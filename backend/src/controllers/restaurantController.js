const { body, query, param } = require('express-validator');
const Restaurant = require('../models/Restaurant');

const restaurantRules = [
  body('name').trim().notEmpty().isLength({ max: 200 }),
  body('address').trim().notEmpty(),
  body('city').trim().notEmpty(),
];

const getRestaurants = async (req, res, next) => {
  try {
    const { search, city, cuisine, page = 1, limit = 20 } = req.query;
    const filter = { isActive: true };

    if (search) filter.$text = { $search: search };
    if (city) filter.city = new RegExp(city, 'i');
    if (cuisine) filter.cuisineType = { $in: cuisine.split(',') };

    const skip = (parseInt(page) - 1) * parseInt(limit);
    const [restaurants, total] = await Promise.all([
      Restaurant.find(filter).skip(skip).limit(parseInt(limit)).sort({ createdAt: -1 }),
      Restaurant.countDocuments(filter),
    ]);

    res.json({
      success: true,
      data: {
        restaurants,
        pagination: { page: parseInt(page), limit: parseInt(limit), total, pages: Math.ceil(total / limit) },
      },
    });
  } catch (err) {
    next(err);
  }
};

const getRestaurantById = async (req, res, next) => {
  try {
    const restaurant = await Restaurant.findOne({ _id: req.params.id, isActive: true });
    if (!restaurant) return res.status(404).json({ success: false, message: 'Restaurant not found' });
    res.json({ success: true, data: restaurant });
  } catch (err) {
    next(err);
  }
};

const createRestaurant = async (req, res, next) => {
  try {
    const restaurant = new Restaurant({ ...req.body, createdBy: req.user._id });
    await restaurant.save();
    res.status(201).json({ success: true, data: restaurant });
  } catch (err) {
    next(err);
  }
};

const updateRestaurant = async (req, res, next) => {
  try {
    const restaurant = await Restaurant.findByIdAndUpdate(req.params.id, req.body, { new: true, runValidators: true });
    if (!restaurant) return res.status(404).json({ success: false, message: 'Restaurant not found' });
    res.json({ success: true, data: restaurant });
  } catch (err) {
    next(err);
  }
};

const deleteRestaurant = async (req, res, next) => {
  try {
    const restaurant = await Restaurant.findByIdAndUpdate(req.params.id, { isActive: false }, { new: true });
    if (!restaurant) return res.status(404).json({ success: false, message: 'Restaurant not found' });
    res.json({ success: true, message: 'Restaurant deleted' });
  } catch (err) {
    next(err);
  }
};

module.exports = { getRestaurants, getRestaurantById, createRestaurant, updateRestaurant, deleteRestaurant, restaurantRules };
