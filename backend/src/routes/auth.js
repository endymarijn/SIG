const router = require('express').Router();
const { register, login, getMe, registerRules, loginRules } = require('../controllers/authController');
const { validate } = require('../middleware/validation');
const { authenticate } = require('../middleware/auth');

router.post('/register', registerRules, validate, register);
router.post('/login', loginRules, validate, login);
router.get('/me', authenticate, getMe);

module.exports = router;
