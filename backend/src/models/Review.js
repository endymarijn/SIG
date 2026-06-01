const mongoose = require('mongoose');

const reviewSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    restaurantId: { type: mongoose.Schema.Types.ObjectId, ref: 'Restaurant', required: true },
    rating: { type: Number, required: true, min: 1, max: 5 },
    comment: { type: String, trim: true, maxlength: 1000 },
    imageUrl: String,
  },
  { timestamps: true }
);

reviewSchema.index({ restaurantId: 1 });
reviewSchema.index({ userId: 1, restaurantId: 1 }, { unique: true });

module.exports = mongoose.model('Review', reviewSchema);
