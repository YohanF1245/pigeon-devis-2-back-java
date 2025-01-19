# Business rules
## Table of Contents

- [Account management](#account-management)
- [Dashboard management](#dashboard-management)
- [Profile management](#profile-management)
- [Postal address management](#postal-address-management)
- [Business management](#business-management)
- [Performance management](#performance-management)
- [Estimate management](#estimate-management)
- [Customer management](#customer-management)
- [Invoice management](#invoice-management)
- [Expense management](#expense-management)
## Account management
- B.R. 1: A user must be logged in to use the software
- B.R. 2: A user must input a valid email to create an account
- B.R. 3: User email must not be already used
- B.R. 4: A user must input a valid password to create an account
- B.R. 5: Account must be verified by mail confirmation
- B.R. 6: User can't request a password reset link

## Dashboard management
- B.R. 7: The dashboard shows global statistics about the business activities
- B.R. 8: The dashboard has a graph showing income and outcome
- B.R. 9: Income is split between money from emitted invoice, emitted estimate, or paid invoice
- B.R. 10: The dashboard shows notification about the number of estimates not paid on time
- B.R. 11: The dashboard shows notification about the number of invoices not paid on time
- B.R. 12: The dashboard shows the five last invoices, estimates, and expenses in a table
- B.R. 13: The dashboard shows a balance with income and outcome

## Profile management
- B.R. 14: A user has a first name (required)
- B.R. 15: A user has a last name (required)
- B.R. 16: A user has an email address (required)
- B.R. 17: A user has a phone number (not required)
- B.R. 18: A user has a postal address (not required)
- B.R. 19: A user with an associated business is a business owner
- B.R. 20: A user can upload his signature

## Postal address management
- B.R. 21: Postal address has a street number
- B.R. 22: Postal address has a street name
- B.R. 23: Postal address has a ZIP code
- B.R. 24: Postal address has a city
- B.R. 25: Postal address may have an optional field

## Business management
- B.R. 26: A business is created by a user
- B.R. 27: A business is owned by a user
- B.R. 28: A business has a SIRET
- B.R. 29: A business has an APE code
- B.R. 30: A business has a tax code
- B.R. 31: A business can have a logo
- B.R. 32: Businesses are only visible by their owner

## Performance management
- B.R. 33: A performance can be a service provided or a good sold
- B.R. 34: A performance is related to a company
- B.R. 35: A performance has a price
- B.R. 36: A performance can be created, edited, or deleted by a business owner
- B.R. 37: A performance is associated with a tax

## Estimate management
- B.R. 38: An estimate can be created, edited, or deleted by a business owner
- B.R. 39: An estimate has at least one performance associated
- B.R. 40: An estimate can have the same performance associated multiple times
- B.R. 41: An estimate has a customer
- B.R. 42: An estimate has a creation date
- B.R. 43: An estimate may have a discount
- B.R. 44: An estimate has a state (emitted or accepted)
- B.R. 45: An estimate has an expiration date
- B.R. 46: An estimate has an expected delivery time

## Customer management
- B.R. 47: A user can create, update, or edit a customer
- B.R. 48: A customer can be a professional or individual
- B.R. 49: A professional customer has a business name (required)
- B.R. 50: An individual customer has a first name (required)
- B.R. 51: An individual customer has a last name (required)
- B.R. 52: A customer has an email (optional)
- B.R. 53: A customer has a phone number (optional)
- B.R. 54: A customer has a postal address (optional)

## Invoice management
- B.R. 55: A business owner can create, update, or edit an invoice
- B.R. 56: An invoice requires an approved estimate
- B.R. 57: An invoice has two states (emitted or paid)
- B.R. 58: An invoice has a price surcharge (set by default to zero)
- B.R. 59: An invoice has a payment limit date
- B.R. 60: An invoice has a payment date
- B.R. 61: An invoice has a creation date

## Expense management
- B.R. 62: A business owner can create, update, or edit an expense
- B.R. 63: An expense has a title
- B.R. 64: An expense has a price
- B.R. 65: An expense has a date
