function addToCart(action) {
    document.addForm.action = action;
    document.addForm.submit();
    alert("도서가 장바구니에 추가되었습니다!");
}

function removeFromCart(action) {
    document.removeForm.action = action;
    document.removeForm.submit();
    window.location.reload();
}

function clearCart() {
    document.clearForm.submit();
    window.location.reload();
}
function deleteConfirm(bookId) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/books/remove';

        const bookIdInput = document.createElement('input');
        bookIdInput.type = 'hidden';
        bookIdInput.name = 'bookId';
        bookIdInput.value = bookId;
        form.appendChild(bookIdInput);

        // CSRF 토큰 추가
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfInput = document.createElement('input');
        csrfInput.type = 'hidden';
        csrfInput.name = '_csrf';
        csrfInput.value = csrfToken;
        form.appendChild(csrfInput);

        document.body.appendChild(form);
        form.submit();
    }
}

