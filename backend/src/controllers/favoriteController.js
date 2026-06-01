const { body } = require('express-validator');
const Favorite = require('../models/Favorite');

const favoriteRules = [
  body('type').isIn(['restaurant', 'dish']),
  body('itemId').isMongoId(),
];

const getFavorites = async (req, res, next) => {
  try {
    const { type } = req.query;
    const filter = { userId: req.user._id };
    if (type) filter.type = type;

    const favorites = await Favorite.find(filter).populate('itemId').sort({ createdAt: -1 });
    res.json({ success: true, data: favorites });
  } catch (err) {
    next(err);
  }
};

const addFavorite = async (req, res, next) => {
  try {
    const { type, itemId } = req.body;
    const itemModel = type === 'restaurant' ? 'Restaurant' : 'Dish';

    const favorite = await Favorite.findOneAndUpdate(
      { userId: req.user._id, type, itemId },
      { userId: req.user._id, type, itemId, itemModel },
      { upsert: true, new: true, setDefaultsOnInsert: true }
    );

    res.status(201).json({ success: true, data: favorite });
  } catch (err) {
    next(err);
  }
};

const removeFavorite = async (req, res, next) => {
  try {
    const deleted = await Favorite.findOneAndDelete({ userId: req.user._id, _id: req.params.id });
    if (!deleted) return res.status(404).json({ success: false, message: 'Favorite not found' });
    res.json({ success: true, message: 'Removed from favorites' });
  } catch (err) {
    next(err);
  }
};

module.exports = { getFavorites, addFavorite, removeFavorite, favoriteRules };
