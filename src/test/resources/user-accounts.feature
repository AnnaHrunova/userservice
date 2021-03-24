Feature: User Accounts

  Background:
    Given Registered user with email accounts: "email1@email.com" and "email2@email.com" and "expired_subscription@email.com"

  Scenario: User Story 2: User updates quota for second account
    When User has "FREE_TEAR" active quota for account "email2@email.com"
    Then Summary data for account "email2@email.com" is
      | email              | email2@email.com   |
      | owner              | Firstname Lastname |
      | usedGB             | 0,00 GB            |
      | totalGB            | 1,00 GB            |
      | latestSubscription | FREE_TEAR          |
      | freeSpace          | 100,00 %           |
      | price              | FREE               |

    When User adds "BASIC" quota for account "email2@email.com"
    Then User has "BASIC" active quota for account "email2@email.com"
    Then Summary data for account "email2@email.com" is
      | email              | email2@email.com     |
      | owner              | Firstname Lastname   |
      | usedGB             | 0,00 GB              |
      | totalGB            | 10,00 GB             |
      | latestSubscription | BASIC                |
      | freeSpace          | 100,00 %             |
      | price              | 1,00 USD per 30 days |

  Scenario: User Story 6: Support Agent gets meta information
    When Support Agent gets "email1@email.com" account information
    Then Summary data for account "email1@email.com" is
      | email              | email1@email.com   |
      | owner              | Firstname Lastname |
      | usedGB             | 0,00 GB            |
      | totalGB            | 1,00 GB            |
      | latestSubscription | FREE_TEAR          |
      | freeSpace          | 100,00 %           |
      | price              | FREE               |

    When Support Agent gets "email2@email.com" account information
    Then Summary data for account "email2@email.com" is
      | email              | email2@email.com   |
      | owner              | Firstname Lastname |
      | usedGB             | 0,00 GB            |
      | totalGB            | 1,00 GB            |
      | latestSubscription | FREE_TEAR          |
      | freeSpace          | 100,00 %           |
      | price              | FREE               |

    When Support Agent gets "expired_subscription@email.com" account information
    Then Summary data for account "expired_subscription@email.com" is
      | email              | expired_subscription@email.com |
      | owner              | Firstname Lastname             |
      | usedGB             | 0,50 GB                        |
      | totalGB            | 10,00 GB                       |
      | latestSubscription | BASIC                          |
      | freeSpace          | 50,00 %                        |
      | price              | 1,00 USD per 30 days           |
    And Quota subscription for "expired_subscription@email.com" is expired 10 days ago

    When User adds "BASIC" quota for account "email2@email.com"
    And Support Agent gets "email2@email.com" account information
    Then Summary data for account "email2@email.com" is
      | email              | email2@email.com     |
      | owner              | Firstname Lastname   |
      | usedGB             | 0,00 GB              |
      | totalGB            | 10,00 GB             |
      | latestSubscription | BASIC                |
      | freeSpace          | 100,00 %             |
      | price              | 1,00 USD per 30 days |
    And Quota subscription for "email2@email.com" is active from today
    And Quota subscription for "email2@email.com" expires after 30 days

    When User uses 4 GB in account "email2@email.com"
    And Support Agent gets "email2@email.com" account information
    Then Summary data for account "email2@email.com" is
      | email              | email2@email.com     |
      | owner              | Firstname Lastname   |
      | usedGB             | 4,00 GB              |
      | totalGB            | 10,00 GB             |
      | latestSubscription | BASIC                |
      | freeSpace          | 60,00 %              |
      | price              | 1,00 USD per 30 days |

    And Support Agent gets "email1@email.com" account information
    Then Summary data for account "email1@email.com" is
      | email              | email1@email.com   |
      | owner              | Firstname Lastname |
      | usedGB             | 0,00 GB            |
      | totalGB            | 1,00 GB            |
      | latestSubscription | FREE_TEAR          |
      | freeSpace          | 100,00 %           |
      | price              | FREE               |
    And Quota subscription for "email1@email.com" is active from today
    And Quota subscription for "email1@email.com" is unlimited

    When Support Agent gets current user information
    And Summary data for current user is
      | owner        | Firstname Lastname                                                 |
      | emails       | email1@email.com, email2@email.com, expired_subscription@email.com |
      | totalUsedGB  | 4,50 GB                                                            |
      | totalQuotaGB | 12,00 GB                                                           |
      | currentCosts | 1,00 USD                                                           |