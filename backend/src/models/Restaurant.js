const mongoose = require('mongoose');

const openingHoursSchema = new mongoose.Schema(
  {
    day: { type: String, enum: ['monday','tuesday','wednesday','thursday','friday','saturday','sunday'] },
    open: String,
    close: String,
    isClosed: { type: Boolean, default: false },
  },
  { _id: false }
);

const restaurantSchema = new mongoose.Schema(
  {
    name: { type: String, required: true, trim: true, maxlength: 200 },
    description: { type: String, trim: true, maxlength: 2000 },
    address: { type: String, required: true, trim: true },
    city: { type: String, required: true, trim: true },
    postalCode: { type: String, trim: true },
    country: { type: String, default: 'Netherlands', trim: true },
    imageUrl: String,
    cuisineType: [{ type: String, trim: true }],
    openingHours: [openingHoursSchema],
    phoneNumber: String,
    website: String,
    isActive: { type: Boolean, default: true },
    createdBy: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  },
  {
    timestamps: true,
    toJSON: { virtuals: true },
  }
);

restaurantSchema.index({ name: 'text', city: 'text', description: 'text' });
restaurantSchema.index({ city: 1 });
restaurantSchema.index({ cuisineType: 1 });

module.exports = mongoose.model('Restaurant', restaurantSchema);
