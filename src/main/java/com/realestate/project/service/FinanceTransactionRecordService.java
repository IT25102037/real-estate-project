package com.realestate.project.service;

import com.realestate.project.dto.BankAccountDTO;
import com.realestate.project.dto.FinanceTransactionRecordDTO;
import com.realestate.project.dto.InvoiceResponse;
import com.realestate.project.dto.PaymentHistoryResponse;
import com.realestate.project.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceTransactionRecordService {

    private final PaymentService paymentService;
    private final PaymentHistoryService paymentHistoryService;
    private final InvoiceService invoiceService;
    private final BankAccountService bankAccountService;
    private final FinanceDashboardService financeDashboardService;

    public FinanceTransactionRecordService(PaymentService paymentService,
                                           PaymentHistoryService paymentHistoryService,
                                           InvoiceService invoiceService,
                                           BankAccountService bankAccountService,
                                           FinanceDashboardService financeDashboardService) {
        this.paymentService = paymentService;
        this.paymentHistoryService = paymentHistoryService;
        this.invoiceService = invoiceService;
        this.bankAccountService = bankAccountService;
        this.financeDashboardService = financeDashboardService;
    }

    public List<FinanceTransactionRecordDTO> getAllRecords() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        List<PaymentHistoryResponse> histories = paymentHistoryService.getAllPaymentHistory();
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices();
        List<BankAccountDTO> bankAccounts = bankAccountService.getAllBankAccounts();
        String homeSummary = formatHomeSummary();

        Map<Long, List<PaymentHistoryResponse>> historyByPayment = histories.stream()
                .collect(Collectors.groupingBy(PaymentHistoryResponse::getPaymentId));

        Map<Long, InvoiceResponse> invoiceByPayment = invoices.stream()
                .filter(inv -> inv.getPaymentId() != null)
                .collect(Collectors.toMap(
                        InvoiceResponse::getPaymentId,
                        inv -> inv,
                        (left, right) -> left
                ));

        Map<Long, BankAccountDTO> bankById = bankAccounts.stream()
                .collect(Collectors.toMap(BankAccountDTO::getId, account -> account, (left, right) -> left));

        List<FinanceTransactionRecordDTO> records = new ArrayList<>();
        for (PaymentResponse payment : payments) {
            Long paymentId = payment.getPaymentId();
            records.add(FinanceTransactionRecordDTO.builder()
                    .id(paymentId)
                    .payment(formatPayment(payment))
                    .paymentHistory(formatPaymentHistory(historyByPayment.get(paymentId)))
                    .invoice(formatInvoice(invoiceByPayment.get(paymentId)))
                    .home(homeSummary)
                    .bankAccount(formatBankAccount(bankById.get(payment.getBankAccountId())))
                    .build());
        }

        records.sort(Comparator.comparing(FinanceTransactionRecordDTO::getId, Comparator.nullsLast(Comparator.reverseOrder())));
        return records;
    }

    private String formatHomeSummary() {
        var summary = financeDashboardService.getSummary();
        return "Finance Home · Collected $" + (summary.getTotalRevenue() != null ? summary.getTotalRevenue() : java.math.BigDecimal.ZERO)
                + " · Pending " + summary.getPendingInvoices()
                + " · Accounts " + summary.getTotalBankAccounts();
    }

    private String formatPayment(PaymentResponse payment) {
        if (payment == null) {
            return "—";
        }
        BigDecimal amount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
        return payment.getPaymentType() + " · " + payment.getPaymentStatus()
                + " · $" + amount + " · Tx " + (payment.getTransactionId() != null ? payment.getTransactionId() : "—");
    }

    private String formatPaymentHistory(List<PaymentHistoryResponse> entries) {
        if (entries == null || entries.isEmpty()) {
            return "No history";
        }
        PaymentHistoryResponse latest = entries.stream()
                .max(Comparator.comparing(PaymentHistoryResponse::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(entries.get(0));
        return latest.getOldStatus() + " → " + latest.getNewStatus()
                + " · " + (latest.getUpdatedAt() != null ? latest.getUpdatedAt().toLocalDate() : "—");
    }

    private String formatInvoice(InvoiceResponse invoice) {
        if (invoice == null) {
            return "No invoice";
        }
        return invoice.getInvoiceNumber() + " · $" + invoice.getTotalAmount()
                + " · " + (invoice.getIssuedDate() != null ? invoice.getIssuedDate().toLocalDate() : "—");
    }

    private String formatBankAccount(BankAccountDTO account) {
        if (account == null) {
            return "—";
        }
        return account.getBankName() + " · " + account.getAccountHolderName()
                + " · " + maskAccount(account.getAccountNumber());
    }

    private String maskAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "—";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}
