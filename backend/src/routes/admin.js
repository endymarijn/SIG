const router = require('express').Router();
const User = require('../models/User');
const { authenticate, requireAdmin } = require('../middleware/auth');

router.use(authenticate, requireAdmin);

router.get('/users', async (req, res, next) => {
  try {
    const { page = 1, limit = 20 } = req.query;
    const skip = (parseInt(page) - 1) * parseInt(limit);
    const [users, total] = await Promise.all([
      User.find().select('-passwordHash').skip(skip).limit(parseInt(limit)).sort({ createdAt: -1 }),
      User.countDocuments(),
    ]);
    res.json({ success: true, data: { users, pagination: { page: parseInt(page), limit: parseInt(limit), total } } });
  } catch (err) {
    next(err);
  }
});

router.put('/users/:id/role', async (req, res, next) => {
  try {
    const { role } = req.body;
    if (!['user', 'admin'].includes(role)) {
      return res.status(400).json({ success: false, message: 'Invalid role' });
    }
    const user = await User.findByIdAndUpdate(req.params.id, { role }, { new: true }).select('-passwordHash');
    if (!user) return res.status(404).json({ success: false, message: 'User not found' });
    res.json({ success: true, data: user });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
