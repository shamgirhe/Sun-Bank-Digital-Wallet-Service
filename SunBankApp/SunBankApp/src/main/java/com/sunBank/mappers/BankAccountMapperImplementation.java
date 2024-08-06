package com.sunBank.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunBank.dtos.AccountOperationDto;
import com.sunBank.dtos.CurrentBankAccountDto;
import com.sunBank.dtos.CustomerDto;
import com.sunBank.dtos.SavingBankAccountDto;
import com.sunBank.entities.AccountOperation;
import com.sunBank.entities.CurrentAccount;
import com.sunBank.entities.Customer;
import com.sunBank.entities.SavingAccount;

@Service
public class BankAccountMapperImplementation {
	
	@Autowired   // here that class that variable mapper implemenation
	BankAccountMapperImplementation bankAccountMapperImplementation;
	
	public CustomerDto fromCustomer(Customer customer)
	{
		CustomerDto customerDto = new CustomerDto();
		BeanUtils.copyProperties(customer, customerDto);
		return customerDto;
	}
	
	public Customer fromCustomerDto(CustomerDto customerDto)
	{
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDto, customer);
		return customer;
	}
	
	public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAccount)
	{
		SavingBankAccountDto savingBankAccountDto = new SavingBankAccountDto();
		BeanUtils.copyProperties(savingAccount, savingBankAccountDto);
		savingBankAccountDto.setCustomerDTO(bankAccountMapperImplementation.fromCustomer(savingAccount.getCustomer()));
		savingBankAccountDto.setType("saving account");
		return savingBankAccountDto;
	}
	
	public SavingAccount fromSavingBankAccountDto(SavingBankAccountDto savingBankAccountDto)
	{
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(savingBankAccountDto, savingAccount);
		savingAccount.setCustomer(bankAccountMapperImplementation.fromCustomerDto(savingBankAccountDto.getCustomerDTO()));
		return savingAccount;
	}
	
	// From Current Bank Account Dto ----> Current Account 
	
	public CurrentAccount fromCurrentBankAccountDto(CurrentBankAccountDto currentBankAccountDto)
	{
		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentBankAccountDto, currentAccount);
		currentAccount.setCustomer(bankAccountMapperImplementation.fromCustomerDto(currentBankAccountDto.getCustomerDTO()));
		return currentAccount;
	}
	
	// From CurrentAccount ----> CurrentBankAccountDto 
	
	public CurrentBankAccountDto fromCurrentBankAccount(CurrentAccount currentAccount)
	{
		CurrentBankAccountDto currentBankAccountDto = new CurrentBankAccountDto();
		BeanUtils.copyProperties(currentAccount, currentBankAccountDto);
		currentBankAccountDto.setCustomerDTO(bankAccountMapperImplementation.fromCustomer(currentAccount.getCustomer()));
		currentBankAccountDto.setType("current account");
		return currentBankAccountDto;
	}
	
	// Account Operation ----> AccountOperation Dtos 
	public AccountOperationDto fromAccountOperation(AccountOperation accountOperation)
	{
		AccountOperationDto accountOperationDto = new AccountOperationDto();
		BeanUtils.copyProperties(accountOperation, accountOperationDto);
		accountOperationDto.setAccountId(accountOperation.getBankAccount().getId());
		return accountOperationDto;
	}
}
