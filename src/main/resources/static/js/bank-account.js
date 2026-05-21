document.addEventListener("DOMContentLoaded", function () {

    const bankAccountForm = document.getElementById("bankAccountForm");

    if (bankAccountForm) {
        bankAccountForm.addEventListener("submit", async function (e) {

            e.preventDefault();

            const bankAccountData = {
                bankName: document.getElementById("bankName").value,
                accountHolderName: document.getElementById("accountHolderName").value,
                accountNumber: document.getElementById("accountNumber").value,
                branchName: document.getElementById("branchName").value,
                userId: document.getElementById("userId").value
            };

            try {
                const response = await fetch("/api/bank-accounts", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(bankAccountData)
                });

                if (!response.ok) {
                    throw new Error("Failed to save bank account");
                }

                document.getElementById("message").innerText =
                    "Bank account saved successfully";

                bankAccountForm.reset();

            } catch (error) {
                console.log(error);

                document.getElementById("message").innerText =
                    "Bank account save failed";
            }
        });
    }
});

async function loadBankAccounts() {

    const bankAccountList = document.getElementById("bankAccountList");

    try {
        const response = await fetch("/api/bank-accounts");

        if (!response.ok) {
            throw new Error("Failed to load bank accounts");
        }

        const accounts = await response.json();

        let html = "";

        if (accounts.length === 0) {
            html = "<p>No bank accounts found.</p>";
        }

        accounts.forEach(account => {
            html += `
                <div class="card">
                    <p><b>Bank:</b> ${account.bankName}</p>
                    <p><b>Account Holder:</b> ${account.accountHolderName}</p>
                    <p><b>Account Number:</b> ${account.accountNumber}</p>
                    <p><b>Branch:</b> ${account.branchName}</p>
                    <p><b>User ID:</b> ${account.userId}</p>
                </div>
            `;
        });

        bankAccountList.innerHTML = html;

    } catch (error) {
        console.log(error);

        bankAccountList.innerHTML =
            "<p style='color:red;'>Bank accounts loading failed.</p>";
    }
}