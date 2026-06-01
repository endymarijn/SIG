/**
 * Harris-Benedict equation for BMR.
 * activityLevel: sedentary | light | moderate | active | very_active
 */
const calculateBMR = (weight, height, age, gender) => {
  if (gender === 'male') {
    return 88.362 + 13.397 * weight + 4.799 * height - 5.677 * age;
  }
  return 447.593 + 9.247 * weight + 3.098 * height - 4.330 * age;
};

const ACTIVITY_MULTIPLIERS = {
  sedentary: 1.2,
  light: 1.375,
  moderate: 1.55,
  active: 1.725,
  very_active: 1.9,
};

const calculateTDEE = (bmr, activityLevel) =>
  Math.round(bmr * (ACTIVITY_MULTIPLIERS[activityLevel] || 1.2));

const calculateGoalCalories = (tdee, goal) => {
  const adjustments = { lose_weight: -500, maintenance: 0, gain_muscle: 300 };
  return tdee + (adjustments[goal] || 0);
};

const calculateProteinGoal = (weight, goal) => {
  const multipliers = { lose_weight: 2.2, maintenance: 1.8, gain_muscle: 2.4 };
  return Math.round(weight * (multipliers[goal] || 1.8));
};

const generateNutritionLabels = (dish) => {
  const labels = [];
  const { calories, protein, carbs, fat, fiber } = dish;

  if (protein >= 25) labels.push('Hoog Eiwit');
  if (protein >= 30 && calories >= 600) labels.push('Bulk Friendly');
  if (protein >= 20 && calories <= 500) labels.push('Cut Friendly');
  if (carbs <= 10 && fat >= 15) labels.push('Keto Friendly');
  if (calories <= 400) labels.push('Caloriearm');
  if (fiber >= 5) labels.push('Vezelrijk');

  return labels;
};

module.exports = { calculateBMR, calculateTDEE, calculateGoalCalories, calculateProteinGoal, generateNutritionLabels };
