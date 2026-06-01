const { body } = require('express-validator');
const User = require('../models/User');
const { generateToken } = require('../utils/jwtUtils');
const { calculateBMR, calculateTDEE, calculateGoalCalories, calculateProteinGoal } = require('../utils/nutritionUtils');

// Validation rules
const registerRules = [
  body('name').trim().notEmpty().isLength({ max: 100 }),
  body('email').isEmail().normalizeEmail(),
  body('password').isLength({ min: 8 }).withMessage('Password must be at least 8 characters'),
];

const loginRules = [
  body('email').isEmail().normalizeEmail(),
  body('password').notEmpty(),
];

const register = async (req, res, next) => {
  try {
    const { name, email, password, age, weight, height, gender, activityLevel, goal } = req.body;

    const existing = await User.findOne({ email });
    if (existing) {
      return res.status(409).json({ success: false, message: 'Email already registered' });
    }

    const user = new User({ name, email, passwordHash: password, age, weight, height, gender, activityLevel, goal });
    await user.save();

    const token = generateToken({ userId: user._id, role: user.role });

    res.status(201).json({
      success: true,
      message: 'Registration successful',
      data: { token, user: user.toPublicJSON() },
    });
  } catch (err) {
    next(err);
  }
};

const login = async (req, res, next) => {
  try {
    const { email, password } = req.body;

    const user = await User.findOne({ email, isActive: true });
    if (!user || !(await user.comparePassword(password))) {
      return res.status(401).json({ success: false, message: 'Invalid credentials' });
    }

    const token = generateToken({ userId: user._id, role: user.role });

    res.json({
      success: true,
      data: { token, user: user.toPublicJSON() },
    });
  } catch (err) {
    next(err);
  }
};

const getMe = async (req, res) => {
  const user = req.user;
  let nutritionData = null;

  if (user.weight && user.height && user.age && user.gender) {
    const bmr = calculateBMR(user.weight, user.height, user.age, user.gender);
    const tdee = calculateTDEE(bmr, user.activityLevel);
    nutritionData = {
      bmr: Math.round(bmr),
      tdee,
      goalCalories: calculateGoalCalories(tdee, user.goal),
      proteinGoal: calculateProteinGoal(user.weight, user.goal),
    };
  }

  res.json({ success: true, data: { user: user.toPublicJSON(), nutrition: nutritionData } });
};

module.exports = { register, login, getMe, registerRules, loginRules };
