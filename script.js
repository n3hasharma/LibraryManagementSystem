// Element references
const loginPage = document.getElementById('loginPage');
const welcomePage = document.getElementById('welcomePage');
const libraryPage = document.getElementById('libraryPage');
const loginMsg = document.getElementById('loginMsg');

// Page display handler
function showPage(page) {
    [loginPage, welcomePage, libraryPage].forEach(p => p.style.display = 'none');
    page.style.display = 'block';
}
showPage(loginPage);

// Admin login
document.getElementById('loginBtn').addEventListener('click', () => {
    const user = document.getElementById('username').value.trim();
    const pass = document.getElementById('password').value.trim();
    if (user === 'admin' && pass === 'admin123') {
        loginMsg.textContent = "✅ Admin login successful!";
        loginMsg.className = "msg success";
        setTimeout(() => showPage(welcomePage), 1000);
    } else {
        loginMsg.textContent = "❌ Invalid username or password!";
        loginMsg.className = "msg error";
    }
});

// OTP simulation
let generatedOTP = null;
document.getElementById('otpBtn').addEventListener('click', () => {
    const email = document.getElementById('email').value.trim();
    if (!email) {
        loginMsg.textContent = "⚠️ Enter your email!";
        loginMsg.className = "msg error";
        return;
    }
    generatedOTP = Math.floor(100000 + Math.random() * 900000);
    document.getElementById('otpField').classList.remove('hidden');
    loginMsg.textContent = `✅ OTP sent to ${email}: ${generatedOTP} (simulated)`;
    loginMsg.className = "msg success";
});

document.getElementById('verifyOtpBtn').addEventListener('click', () => {
    const entered = document.getElementById('otpInput').value.trim();
    if (entered == generatedOTP) {
        loginMsg.textContent = "✅ OTP Verified!";
        loginMsg.className = "msg success";
        setTimeout(() => showPage(welcomePage), 800);
    } else {
        loginMsg.textContent = "❌ Incorrect OTP!";
        loginMsg.className = "msg error";
    }
});

// Welcome page navigation
document.getElementById('enterBtn').addEventListener('click', () => showPage(libraryPage));
document.getElementById('logoutBtn').addEventListener('click', () => showPage(loginPage));
document.getElementById('logoutBtn2').addEventListener('click', () => showPage(loginPage));

// Library form logic
const bookForm = document.getElementById('bookForm');
const msg = document.getElementById('msg');

document.getElementById('add').addEventListener('click', () => {
    const id = document.getElementById('bookId').value.trim();
    const title = document.getElementById('title').value.trim();
    const author = document.getElementById('author').value.trim();
    const publisher = document.getElementById('publisher').value.trim();
    const quantity = document.getElementById('quantity').value.trim();

    if (!id || !title || !author || !publisher || !quantity) {
        msg.textContent = "⚠️ Please fill all fields!";
        msg.className = "msg error";
        return;
    }

    if (Number(id) <= 0 || Number(quantity) <= 0) {
        msg.textContent = "⚠️ Book ID & Quantity must be positive!";
        msg.className = "msg error";
        return;
    }

    msg.textContent = "✅ Book Added Successfully!";
    msg.className = "msg success";
    bookForm.reset();
});

document.getElementById('reset').addEventListener('click', () => {
    bookForm.reset();
    msg.textContent = "";
});
