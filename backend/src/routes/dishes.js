const router = require('express').Router();
const { getDishes, getDishById, createDish, updateDish, deleteDish, dishRules } = require('../controllers/dishController');
const { authenticate, requireAdmin } = require('../middleware/auth');
const { validate } = require('../middleware/validation');

router.get('/', getDishes);
router.get('/:id', getDishById);

// Admin only
router.post('/', authenticate, requireAdmin, dishRules, validate, createDish);
router.put('/:id', authenticate, requireAdmin, validate, updateDish);
router.delete('/:id', authenticate, requireAdmin, deleteDish);

module.exports = router;
