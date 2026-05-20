const API_URL = '';

function getToken() {
    return localStorage.getItem('token');
}

function authHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${getToken()}`
    };
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = '/login.html';
}

function checkAuth() {
    if (!getToken()) {
        window.location.href = '/login.html';
    }
}