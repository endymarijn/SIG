const mongoose = require('mongoose');
const { generateNutritionLabels } = require('../utils/nutritionUtils');

const VALID_TAGS = [
  'hoog_eiwit', 'bulk_friendly', 'cut_friendly', 'keto_friendly',
  'caloriearm', 'vezelrijk', 'vegetarisch', 'vegan', 'glutenvrij', 'lactosevrij',
];

const dishSchema = new mongoose.Schema(
  {
    restaurantId: { type: mongoose.Schema.Types.ObjectId, ref: 'Restaurant', required: true, index: true },
    name: { type: String, required: true, trim: true, maxlength: 200 },
    description: { type: String, trim: true, maxlength: 2000 },
    imageUrl: String,
    category: { type: String, trim: true }, // e.g. Hoofdgerecht, Bijgerecht
    price: { type: Number, min: 0 },

    // Nutrition per serving (grams unless noted)
    servingSize: { type: Number, default: 100 }, // in grams
    calories: { type: Number, required: true, min: 0 },
    protein: { type: Number, required: true, min: 0 },
    carbs: { type: Number, required: true, min: 0 },
    fat: { type: Number, required: true, min: 0 },
    fiber: { type: Number, default: 0, min: 0 },
    sugar: { type: Number, default: 0, min: 0 },
    sodium: { type: Number, default: 0, min: 0 }, // mg

    tags: [{ type: String, enum: VALID_TAGS }],
    isAvailable: { type: Boolean, default: true },
    createdBy: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  },
  {
    timestamps: true,
    toJSON: { virtuals: true },
  }
);

dishSchema.index({ restaurantId: 1, isAvailable: 1 });
dishSchema.index({ name: 'text', description: 'text' });
dishSchema.index({ tags: 1 });

// Auto-generate nutrition labels before save
dishSchema.pre('save', function (next) {
  this.tags = [...new Set([...this.tags, ...generateNutritionLabels(this).map(l =>
    l.toLowerCase().replace(/ /g, '_')
  ).filter(t => VALID_TAGS.includes(t))])];
  next();
});

module.exports = mongoose.model('Dish', dishSchema);
