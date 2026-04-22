window.onload = function() {
    loadCustomers();
};

function loadCustomers() {
    fetch('http://localhost:8080/api/customers')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('customerTableBody');
            tableBody.innerHTML = '';

            data.forEach(customer => {
                const row = `<tr>
                    <td>#${customer.id}</td>
                    <td style="font-weight:600; text-align:left;">${customer.name}</td>
                    <td style="text-align:left;">
                        <div style="font-size: 13px;">📧 ${customer.email}</div>
                        <div style="font-size: 13px; color: #94a3b8;">📞 ${customer.phone}</div>
                    </td>
                    <td><span class="property-badge">${customer.propertyType}</span></td>
                    <td style="text-align:left;">📍 ${customer.address}</td>
                    <td>
                        <button onclick="deleteCustomer(${customer.id})" class="btn-remove">Remove</button>
                    </td>
                </tr>`;
                tableBody.innerHTML += row;
            });
            filterCustomers();
        })
        .catch(error => console.error('Error:', error));
}

function filterCustomers() {
    const idFilter = document.getElementById('searchId').value.toUpperCase();
    const generalFilter = document.getElementById('searchGeneral').value.toLowerCase();
    const typeFilter = document.getElementById('propertyFilter').value.toLowerCase();

    const rows = document.getElementById('customerTableBody').getElementsByTagName('tr');
    let visibleCount = 0;

    for (let i = 0; i < rows.length; i++) {
        const id = rows[i].cells[0].textContent.toUpperCase();
        const name = rows[i].cells[1].textContent.toLowerCase();
        const contact = rows[i].cells[2].textContent.toLowerCase();
        const type = rows[i].cells[3].textContent.toLowerCase();
        const address = rows[i].cells[4].textContent.toLowerCase();

        const matchesId = id.includes(idFilter);
        const matchesGeneral = name.includes(generalFilter) || contact.includes(generalFilter) || address.includes(generalFilter);
        const matchesType = typeFilter === "" || type.includes(typeFilter);

        if (matchesId && matchesGeneral && matchesType) {
            rows[i].style.display = "";
            visibleCount++;
        } else {
            rows[i].style.display = "none";
        }
    }
}

function deleteCustomer(id) {
    if(confirm('Are you sure you want to remove lead #' + id + '?')) {
        fetch(`http://localhost:8081/api/customers/${id}`, { method: 'DELETE' })
        .then(() => {
            loadCustomers();
            // Optional: If you want the dashboard count to update immediately if you stayed on the page
        });
    }
}