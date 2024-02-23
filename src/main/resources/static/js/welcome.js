const BASE_URL = '/api/v1/';

document.addEventListener("DOMContentLoaded", () => {
    let user;

    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('userId');
    const uuid = urlParams.get('uuid');

    fetch(`${BASE_URL}user/${userId}?detail=true`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch user data. HTTP error ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            user = data;

            if (uuid === user.uuid) {
                displayWelcomeMessage(user);
            } else {
                logout();
            }
        })
        .catch(error => {
            Toastify({
                text: `Failed to fetch user data: ${error.message}`,
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#FF6347",
            }).showToast();
        });

    fetch(`${BASE_URL}users`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch user list. HTTP error ${response.status}`);
            }
            return response.json();
        })
        .then(users => {
            displayUserList(users);
        })
        .catch(error => {
            Toastify({
                text: `Failed to fetch user list: ${error.message}`,
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#FF6347",
            }).showToast();
        });

    document.getElementById('deleteProfileButton').addEventListener('click', () => {
        const isConfirmed = confirm('Are you sure you want to delete your profile?');

        if (isConfirmed) {
            deleteProfile();
        }
    });

    document.getElementById('updateProfileButton').addEventListener('click', (event) => {
        const updateForm = document.getElementById('updateProfileForm');

        if (updateForm.style.display === 'block') {
            updateForm.style.display = 'none';
        } else {
            document.getElementById('updateName').value = user.name;
            document.getElementById('updateSurname').value = user.surname;
            updateForm.style.display = 'block';
        }
    });

    document.getElementById('submitUpdateButton').addEventListener('click', (event) => {
        event.preventDefault();
        submitUpdateForm();
    });
});

function displayUserList(users) {
    const userListContainer = document.getElementById("userListContainer");

    userListContainer.innerHTML = '';

    const userListHeader = document.createElement('h2');
    userListHeader.className = 'userListHeader';
    userListHeader.textContent = 'Registered users:';
    userListContainer.appendChild(userListHeader);

    for (const user of users) {
        const userElement = document.createElement("div");
        userElement.textContent = `ID: ${user.id}, ${user.name} ${user.surname}`;
        userListContainer.appendChild(userElement);
    }
}

function logout() {
    window.location.replace('index.html');
}

function deleteProfile() {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('userId');

    fetch(`${BASE_URL}user/${userId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                Toastify({
                    text: "Profile deleted successfully!",
                    duration: 3000,
                    gravity: "top",
                    position: "center",
                    close: true,
                    backgroundColor: "#4CAF50",
                }).showToast();
                setTimeout(() => {
                    logout();
                }, 4000);
            } else {
                Toastify({
                    text: "Failed to delete profile. Please try again.",
                    duration: 3000,
                    gravity: "top",
                    position: "center",
                    close: true,
                    backgroundColor: "#FF6347",
                }).showToast();
            }
        })
        .catch(error => {
            Toastify({
                text: "Failed to delete profile. Please try again.",
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#FF6347",
            }).showToast();
        });
}

function submitUpdateForm() {
    const updateName = document.getElementById('updateName').value;
    const updateSurname = document.getElementById('updateSurname').value;

    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('userId');

    const credentials = {
        id: userId,
        name: updateName,
        surname: updateSurname,
    };

    fetch(`${BASE_URL}user`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
    })
        .then(response => response.json())
        .then(updatedUser => {
            Toastify({
                text: "Profile updated successfully!",
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#4CAF50",
            }).showToast();

            displayWelcomeMessage(updatedUser);

            document.getElementById('updateProfileForm').style.display = 'none';
            setTimeout(() => {
                location.reload();
            }, 4000);
        })
        .catch(error => {
            Toastify({
                text: "Failed to update profile. Please try again.",
                duration: 3000,
                gravity: "top",
                position: "center",
                close: true,
                backgroundColor: "#FF6347",
            }).showToast();
        });

    return false;
}

function displayWelcomeMessage(user) {
    if (user && user.name !== undefined) {
        const welcomeMessage = user.surname
            ? `${user.name} ${user.surname}`
            : user.name;

            document.getElementById("welcomeUser").textContent = welcomeMessage;
    } else {
        Toastify({
            text: "Invalid user data in the server response.",
            duration: 3000,
            gravity: "top",
            position: "center",
            close: true,
            backgroundColor: "#FF6347",
        }).showToast();
    };
}
