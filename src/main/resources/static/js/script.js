// Вставь в начало каждой страницы кроме index и login
if (!localStorage.getItem('token')) {
    window.location.href = 'login.html';
}