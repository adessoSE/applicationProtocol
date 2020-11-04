package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;
import lombok.ToString;

@Appendix
@ToString
public class AccountingRecordAppendix extends ApplicationAppendix<AccountingRecord> {

	public AccountingRecordAppendix(final AccountingRecord accountingRecord) {
		super(accountingRecord);
	}

	@Override
	public UUID getOwner() {
		return Accounting.id;
	}
}
