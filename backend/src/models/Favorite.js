const mongoose = require('mongoose');

const favoriteSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    type: { type: String, enum: ['restaurant', 'dish'], required: true },
    itemId: { type: mongoose.Schema.Types.ObjectId, required: true, refPath: 'itemModel' },
    itemModel: { type: String, enum: ['Restaurant', 'Dish'] },
  },
  { timestamps: true }
);

favoriteSchema.index({ userId: 1, type: 1 });
favoriteSchema.index({ userId: 1, itemId: 1, type: 1 }, { unique: true });

module.exports = mongoose.model('Favorite', favoriteSchema);
