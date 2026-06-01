const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const userSchema = new mongoose.Schema(
  {
    name: { type: String, required: true, trim: true, maxlength: 100 },
    email: { type: String, required: true, unique: true, lowercase: true, trim: true },
    passwordHash: { type: String, required: true },
    age: { type: Number, min: 13, max: 120 },
    weight: { type: Number, min: 20, max: 500 }, // kg
    height: { type: Number, min: 50, max: 300 }, // cm
    gender: { type: String, enum: ['male', 'female', 'other'] },
    activityLevel: {
      type: String,
      enum: ['sedentary', 'light', 'moderate', 'active', 'very_active'],
      default: 'moderate',
    },
    goal: {
      type: String,
      enum: ['lose_weight', 'maintenance', 'gain_muscle'],
      default: 'maintenance',
    },
    role: { type: String, enum: ['user', 'admin'], default: 'user' },
    profileImageUrl: String,
    isActive: { type: Boolean, default: true },
  },
  { timestamps: true }
);

userSchema.pre('save', async function (next) {
  if (!this.isModified('passwordHash')) return next();
  this.passwordHash = await bcrypt.hash(this.passwordHash, 12);
  next();
});

userSchema.methods.comparePassword = function (candidatePassword) {
  return bcrypt.compare(candidatePassword, this.passwordHash);
};

userSchema.methods.toPublicJSON = function () {
  const { passwordHash, __v, ...rest } = this.toObject();
  return rest;
};

module.exports = mongoose.model('User', userSchema);
