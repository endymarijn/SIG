const router = require('express').Router();
const { getRestaurants, getRestaurantById, createRestaurant, updateRestaurant, deleteRestaurant, restaurantRules } = require('../controllers/restaurantController');
const { authenticate, requireAdmin } = require('../middleware/auth');
const { validate } = require('../middleware/validation');

router.get('/', getRestaurants);
router.get('/:id', getRestaurantById);

// Admin only
router.post('/', authenticate, requireAdmin, restaurantRules, validate, createRestaurant);
router.put('/:id', authenticate, requireAdmin, validate, updateRestaurant);
router.delete('/:id', authenticate, requireAdmin, deleteRestaurant);

module.exports = router;
