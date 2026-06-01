const router = require('express').Router();
const { getFavorites, addFavorite, removeFavorite, favoriteRules } = require('../controllers/favoriteController');
const { authenticate } = require('../middleware/auth');
const { validate } = require('../middleware/validation');

router.use(authenticate);

router.get('/', getFavorites);
router.post('/', favoriteRules, validate, addFavorite);
router.delete('/:id', removeFavorite);

module.exports = router;
