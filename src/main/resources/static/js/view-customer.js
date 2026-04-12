// Run this function as soon as the admin dashboard loads
document.addEventListener("DOMContentLoaded", function () {
    fetchCustomers();
});

// Function to get customers from Spring Boot
function fetchCustomers() {
    fetch("http://localhost:8081/api/customers")
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch customers");
            return response.json();
        })
        .then(customers => {
            const tableBody = document.getElementById("customerTableBody");
            tableBody.innerHTML = ""; // Clear the table

            customers.forEach(customer => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.propertyType}</td>
                    <td>${customer.address}</td>
                    <td>
                        <button class="action-btn edit-btn" onclick="editCustomer(${customer.id})">Edit</button>
                        <button class="action-btn delete-btn" onclick="deleteCustomer(${customer.id})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error:", error));
}

// Function to DELETE a customer
function deleteCustomer(id) {
    if (confirm("Are you sure you want to completely delete this customer?")) {
        fetch(`http://localhost:8081/api/customers/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                alert("Customer deleted successfully!");
                fetchCustomers(); // Refresh the table
            } else {
                alert("Failed to delete customer.");
            }
        })
        .catch(error => console.error("Error:", error));
    }
}

// Temporary placeholder for our next task!
function editCustomer(id) {
    alert("We will build the Edit feature next! You clicked ID: " + id);
}

// Function to instantly filter customers
function filterCustomers() {
    const searchInput = document.getElementById("searchInput").value.toLowerCase();
    const propertyFilter = document.getElementById("propertyFilter").value;
    const tableBody = document.getElementById("customerTableBody");
    const rows = tableBody.getElementsByTagName("tr");
    const noResultsMessage = document.getElementById("noResultsMessage");

    let visibleCount = 0;

    for (let i = 0; i < rows.length; i++) {
        const rowText = rows[i].innerText.toLowerCase();
        const propertyCell = rows[i].getElementsByTagName("td")[4];

        if (propertyCell) {
            const propertyTypeValue = propertyCell.innerText;
            const matchesSearch = rowText.includes(searchInput);
            const matchesFilter = (propertyFilter === "All" || propertyFilter === "All Property Types" || propertyTypeValue === propertyFilter);

            if (matchesSearch && matchesFilter) {
                rows[i].style.display = "";
                visibleCount++;
            } else {
                rows[i].style.display = "none";
            }
        }
    }

    if (noResultsMessage) {
        noResultsMessage.style.display = visibleCount === 0 ? "" : "none";
    }
}