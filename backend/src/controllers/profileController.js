const { body } = require('express-validator');
const User = require('../models/User');
const { calculateBMR, calculateTDEE, calculateGoalCalories, calculateProteinGoal } = require('../utils/nutritionUtils');

const updateProfileRules = [
  body('name').optional().trim().isLength({ max: 100 }),
  body('age').optional().isInt({ min: 13, max: 120 }),
  body('weight').optional().isFloat({ min: 20, max: 500 }),
  body('height').optional().isFloat({ min: 50, max: 300 }),
  body('gender').optional().isIn(['male', 'female', 'other']),
  body('activityLevel').optional().isIn(['sedentary', 'light', 'moderate', 'active', 'very_active']),
  body('goal').optional().isIn(['lose_weight', 'maintenance', 'gain_muscle']),
];

const getProfile = async (req, res) => {
  const user = req.user;
  let nutrition = null;

  if (user.weight && user.height && user.age && user.gender) {
    const bmr = calculateBMR(user.weight, user.height, user.age, user.gender);
    const tdee = calculateTDEE(bmr, user.activityLevel);
    nutrition = {
      bmr: Math.round(bmr),
      tdee,
      goalCalories: calculateGoalCalories(tdee, user.goal),
      proteinGoal: calculateProteinGoal(user.weight, user.goal),
    };
  }

  res.json({ success: true, data: { user: user.toPublicJSON(), nutrition } });
};

const updateProfile = async (req, res, next) => {
  try {
    const allowedFields = ['name', 'age', 'weight', 'height', 'gender', 'activityLevel', 'goal', 'profileImageUrl'];
    const updates = Object.fromEntries(Object.entries(req.body).filter(([k]) => allowedFields.includes(k)));

    const user = await User.findByIdAndUpdate(req.user._id, updates, { new: true, runValidators: true }).select('-passwordHash');
    res.json({ success: true, data: user });
  } catch (err) {
    next(err);
  }
};

module.exports = { getProfile, updateProfile, updateProfileRules };
