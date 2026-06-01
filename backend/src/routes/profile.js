const router = require('express').Router();
const { getProfile, updateProfile, updateProfileRules } = require('../controllers/profileController');
const { authenticate } = require('../middleware/auth');
const { validate } = require('../middleware/validation');

router.use(authenticate);

router.get('/', getProfile);
router.put('/', updateProfileRules, validate, updateProfile);

module.exports = router;
