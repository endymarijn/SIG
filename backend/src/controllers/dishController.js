const { body } = require('express-validator');
const Dish = require('../models/Dish');

const dishRules = [
  body('restaurantId').isMongoId(),
  body('name').trim().notEmpty().isLength({ max: 200 }),
  body('calories').isFloat({ min: 0 }),
  body('protein').isFloat({ min: 0 }),
  body('carbs').isFloat({ min: 0 }),
  body('fat').isFloat({ min: 0 }),
];

const getDishes = async (req, res, next) => {
  try {
    const { restaurantId, search, tags, minProtein, maxCalories, page = 1, limit = 20 } = req.query;
    const filter = { isAvailable: true };

    if (restaurantId) filter.restaurantId = restaurantId;
    if (search) filter.$text = { $search: search };
    if (tags) filter.tags = { $in: tags.split(',') };
    if (minProtein) filter.protein = { $gte: parseFloat(minProtein) };
    if (maxCalories) filter.calories = { ...(filter.calories || {}), $lte: parseFloat(maxCalories) };

    const skip = (parseInt(page) - 1) * parseInt(limit);
    const [dishes, total] = await Promise.all([
      Dish.find(filter).populate('restaurantId', 'name city').skip(skip).limit(parseInt(limit)).sort({ createdAt: -1 }),
      Dish.countDocuments(filter),
    ]);

    res.json({
      success: true,
      data: {
        dishes,
        pagination: { page: parseInt(page), limit: parseInt(limit), total, pages: Math.ceil(total / limit) },
      },
    });
  } catch (err) {
    next(err);
  }
};

const getDishById = async (req, res, next) => {
  try {
    const dish = await Dish.findOne({ _id: req.params.id, isAvailable: true }).populate('restaurantId', 'name city address');
    if (!dish) return res.status(404).json({ success: false, message: 'Dish not found' });
    res.json({ success: true, data: dish });
  } catch (err) {
    next(err);
  }
};

const createDish = async (req, res, next) => {
  try {
    const dish = new Dish({ ...req.body, createdBy: req.user._id });
    await dish.save();
    res.status(201).json({ success: true, data: dish });
  } catch (err) {
    next(err);
  }
};

const updateDish = async (req, res, next) => {
  try {
    const dish = await Dish.findByIdAndUpdate(req.params.id, req.body, { new: true, runValidators: true });
    if (!dish) return res.status(404).json({ success: false, message: 'Dish not found' });
    res.json({ success: true, data: dish });
  } catch (err) {
    next(err);
  }
};

const deleteDish = async (req, res, next) => {
  try {
    const dish = await Dish.findByIdAndUpdate(req.params.id, { isAvailable: false }, { new: true });
    if (!dish) return res.status(404).json({ success: false, message: 'Dish not found' });
    res.json({ success: true, message: 'Dish removed' });
  } catch (err) {
    next(err);
  }
};

module.exports = { getDishes, getDishById, createDish, updateDish, deleteDish, dishRules };
