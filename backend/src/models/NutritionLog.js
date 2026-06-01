const mongoose = require('mongoose');

const logEntrySchema = new mongoose.Schema(
  {
    dishId: { type: mongoose.Schema.Types.ObjectId, ref: 'Dish', required: true },
    dishName: String, // snapshot at time of logging
    servings: { type: Number, default: 1, min: 0.25 },
    calories: Number,
    protein: Number,
    carbs: Number,
    fat: Number,
  },
  { _id: false }
);

const nutritionLogSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    date: { type: Date, required: true },
    mealType: { type: String, enum: ['breakfast', 'lunch', 'dinner', 'snack'], required: true },
    entries: [logEntrySchema],
    totalCalories: Number,
    totalProtein: Number,
    totalCarbs: Number,
    totalFat: Number,
    notes: { type: String, trim: true, maxlength: 500 },
  },
  { timestamps: true }
);

nutritionLogSchema.index({ userId: 1, date: -1 });

nutritionLogSchema.pre('save', function (next) {
  this.totalCalories = this.entries.reduce((s, e) => s + (e.calories || 0) * e.servings, 0);
  this.totalProtein = this.entries.reduce((s, e) => s + (e.protein || 0) * e.servings, 0);
  this.totalCarbs = this.entries.reduce((s, e) => s + (e.carbs || 0) * e.servings, 0);
  this.totalFat = this.entries.reduce((s, e) => s + (e.fat || 0) * e.servings, 0);
  next();
});

module.exports = mongoose.model('NutritionLog', nutritionLogSchema);
