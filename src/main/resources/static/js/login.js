const BASE_URL = '/api/v1/';

function addUser() {
    const name = document.getElementById('name').value;
    const surname = document.getElementById('surname').value;
    const personID = document.getElementById('personalId').value;

    if (!name || !personID) {
        Toastify({
            text: 'Please fill in both name and personal ID.',
            duration: 3000,
            gravity: 'top',
            position: 'center',
            close: true,
            backgroundColor: '#FF6347',
        }).showToast();
        return;
    }

    const credentials = {
        name: name,
        surname: surname,
        personID: personID,
    };

    fetch(`${BASE_URL}user`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(error => {
                throw new Error('Failed to add user: ' + error.message);
            });
        }
        return response.json();
    })
    .then(data => {
        const userInfoElement = document.getElementById('user-info');
        if (userInfoElement) {
            userInfoElement.innerText = `Added User: ${data.name} ${data.surname}`;
            Toastify({
                text: "New profile created successfully!",
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#4CAF50",
            }).showToast();
        }
    })
    .catch(error => {
        Toastify({
            text: error.message || 'Failed to add user. Please try again.',
            duration: 3000,
            gravity: 'top',
            position: 'center',
            close: true,
            backgroundColor: '#FF6347',
        }).showToast();
    });

    event.preventDefault();
}

document.addEventListener("DOMContentLoaded", () => {
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        const name = $("#name").val();
        const surname = $("#surname").val() || "";
        const personID = $("#personalId").val();

        const credentials = {
            name: name,
            surname: surname,
            personID: personID,
        };

        fetch(`${BASE_URL}login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Login failed. Please check your credentials.');
            }
            return response.json();
        })
        .then(response => {
            displayWelcomeMessage(response);
        })
        .catch(error => {
            Toastify({
                text: error.message || 'Login failed. Please check your credentials.',
                duration: 3000,
                gravity: 'top',
                position: 'center',
                close: true,
                backgroundColor: '#FF6347',
            }).showToast();
        });
    });

    function displayWelcomeMessage(user) {
        window.location.replace(`welcome.html?userId=${user.id}&uuid=${user.uuid}`);
    }

    function showTooltip(event) {
        event.stopPropagation();
        var tooltip = event.target.nextElementSibling;
        tooltip.classList.toggle('show');
    }

    document.addEventListener('click', (event) => {
        var tooltips = document.getElementsByClassName('tooltiptext');
        for (var i = 0; i < tooltips.length; i++) {
            if (event.target != tooltips[i].previousElementSibling) {
                tooltips[i].classList.remove('show');
            }
        }
    });
});