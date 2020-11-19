package de.adesso.example.application.employment;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.AccountingRecord;
import de.adesso.example.application.accounting.AccountingRecordAppendix;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class EmployeeShoppingBean {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<?> setEmployeeCustomer(
			@Required final ApplicationProtocol<?> state) {

		// if an employee is present, he will be also the customer.
		final Optional<ApplicationAppendix<Employee>> optionalEmployeeAppendix = state
				.getAppendixOfClassT(EmployeeAppendix.class);
		if (optionalEmployeeAppendix.isEmpty()) {
			return state;
		}

		final Customer customer = optionalEmployeeAppendix.get().getContent().getEmployeeCustomer();
		// remove existing customer appendixes
		state.removeAll(CustomerAppendix.class);
		state.addAppendix(new CustomerAppendix(customer));

		return state;
	}

	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<Money> discountEmployee(
			@Required final Article article,
			@Required final Customer customer,
			@Required final Employee employee,
			@Required final ApplicationProtocol<Money> state) {

		final Money price = state.getResult();
		final Money discount = price.multiply(Standard.employeeDiscount).divide(100.0);
		state.setResult(price.subtract(discount));

		state.addAppendix(new EmployeeBenefitAppendix(new EmployeeBenefit(employee, discount)));
		state.addAppendix(new AccountingRecordAppendix(AccountingRecord.builder()
				.debitor(Employment.getEmployeeDiscountCreditor())
				.creditor(customer)
				.value(discount)
				.build()));

		return state;
	}
}
