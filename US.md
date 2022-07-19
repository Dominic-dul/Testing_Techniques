# USER STORIES

| US: SearchItem | 
|------------------------------------------------------------------------------------------------------------------------| 
| Acceptance Test: (AT-SearchItemTest) | 
**US-description:** User fills the search field with the wanted text. After user fills the search field, user is presented with all the results that match the key words in the search field. User selects the most suitable item.

| US: ListItem |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-ListItemTest) | 
**US-description:** The seller enters the following information about an item: image, description, category, status. For the status the seller provides starting price, current price, min bid increment and time before the end of the auction. The seller confirms to list the item with the information provided. After the seller confirms, the item is added to the whole listing pool. 

|US: DeleteItem |
| ------------------------------------------------------------| 
|Acceptance Test: (AT-DeleteItemTest)|
|**US-description:** The seller selects personal listed items. The seller selects the item to delete. The seller confirms to delete a specific item. The item is deleted from the listing. 

| US: EditItem |
| ------------------------------------------------------------ | 
|Acceptance Test: (AT-EditItemTest)| 
|**US-description:** The seller gets the listed items. The seller selects the item to edit. The seller edits the information to change and save. The seller confirms to save the changes. The item edit is saved and the listing is changed.

| US: Login |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-LoginTest)                                   | 
|**US-description:** User enters the nickname. User enters the password. User logs in.

| US: RetrievePersonalData |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-RetrievePersonalDataTest)|
|**US-description:** Precondition: User is logged in. Credentials are sent to email or phone number provided by the user. User enters the confirmation code to change their password or nickname. User enters the new password or nickname.

| US: AccessPersonalPage |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-Access personal page)                                   | 
|**US-description:** If user is a registered user, the personal page displays only the items user has bid on. If user is a seller, personal items enlisted in the auctions are displayed as well. Items are shown with the current bid, remaining time and bidder information. Upon clicking the item, user views additional info.

| US: PlaceBid |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-PlaceBidTest)                                   | 
|**US-description:** User selects the item listing to bid on. User sees the information of the item. User bids according to the required bid amount providing auction ID and the bid amount. If the bid or auction ID does not comply with bidding requirements, an error is shown and the user must bid again. After the user confirms, the bid is placed

| US: ReceiveItem |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-ReceiveItem)                                   | 
| **US-description:** User gets notification that their last bid won the item. User contacts the seller about the item. User confirms to have won the item. User gets the item.

| US: SubscribeForAuction |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-SubscribeForAuctionTest)| 
|**US-description:** User chooses the auction that user wants to subscribe to. User sees all the information about the current bids and information about the listing. User presses the subscribtion button. Subscribed item appears in the personal page. User gets notifications if something changes in the auction that user has subscribed to.

| US: Register |
| ------------------------------------------------------------ | 
| Acceptance Test: (AT-RegisterTest)                                   | 
|**US-description:** The user enters a nickname, a password, their name, address, phone number and email details to the provided spaces. If a nickname, phone or email already exists in registered users database, an error is thrown and the user must enter their information again with a different nickname, phone number and/or email. The user selects to receive the confirmation code via email or phone. User receives and enters the confirmation code. If the code is wrong, an error is shown and the user must provide the correct code to register. User is now registered to the website.

| US: BecomeSeller |
| ------------------------------------------------------------ | 
|Acceptance Test: (AT-BecomeSellerTest)| 
|**US-description:** User provides a SSN to become a seller. If the SSN is incorrect, user is not allowed to become a seller. User becomes a seller.

# ACCEPTANCE TESTS

### Title: AT-SearchItemTest
|Input|Description|Expected value|
|------|------------|--------------|
|A string of characters != null & 0 < length < 100|User enters a valid phrase for search|Items matched by the entered phrase returned|
|Input that does not correspond to the first two cases||Search returns no results|


### Title: AT-ListItemTest
|Input|Description|Expected value|
|------|------------|--------------|
|Item image, description, category or status missing|Seller does not add item image, description, category or status information|Item not listed|
|Min bid increment < 0|Seller enters a negative bid increment|Item not listed|
|Time before auction end < 0|Seller enters a negative time value until auction end|Item not listed|
|Duplicate item entry|Seller tries to add the same item twice|Item not listed|
|Input that does not correspond to the above cases|The seller adds all the necessary item information properly|Item listed|

### Title: AT-DeleteItemTest
|Input|Description|Expected value|
|------|------------|--------------|
|Time until auction end > 0|The seller deletes item still in the auction|Item not deleted|
|Sold item|The seller tries to delete a sold item before money received|Item not deleted|
|Successful delete|The seller sucessfully removes an item from the auction|Item deleted|

### Title: AT-EditItemTest
|Input|Description|Expected value|
|------|------------|--------------|
|No edit made|The seller does not change any item information|Item changes not saved|
|Starting price > current price|Seller updated current price is lower than the starting item price|Item changes not saved|
|Min bid increment < 0|Seller updates a negative bid increment|Item changes not saved|
|Time before auction end < 0|Seller updates a negative time value until auction end|Item changes not saved|
|Input that does not correspond to the above cases||Item changes saved|

### Title: AT-LoginTest
|Input:|Description:|Expected value|
|------|------------|--------------|
|Nickname is string of characters != null & 0 < length < 100 and contains only letters and numbers|Valid input of nickname |Nickname is accepted|
|Password is a string of characters != null & length >= 8 and must contain one upper case, one number and one special character such as:!, @, #, $, %, ^, &, *|Valid input of password| Password is accepted|
|Input that does not correspond to the above cases|Invalid data input |Data not accepted and the user asked to try again|

### Title: AT-RetrievePersonalDataTest
|Input:|Description:|Expected value|
|------|------------|--------------|
|**Pre-cond**: User is already logged-in. Email is a string of characters != null & length > 0 a formatted in this way: prefix@domain. Prefix must be a string which contains only lettters and numbers. Domain must be in a format of a domain|Valid input of email|Email is accepted and confirmation code is sent to the email|
|Phone number with a '+' sign in front provided|Valid input of phone number|Phone is accepted and confirmation code is sent to the phone|
|Input that does not correspond to the above cases|Invalid input of data|Data is not accepted and confirmation code is not sent|

### Title: AT-AccessPersonalPageTest
|Input:|Description:|Expected value|
|------|------------|--------------|
|User registered as simple user|Access to bidded items|User is allowed to see items that user made a bet on|
|User registered as seller|Access to enlisted items|User is allowed to see items which user enlisted in the auction|
|User not registered|Non authorised access|User is not allowed to acces his personal page|

### Title: AT-PlaceBidTest
|Input:|Description:|Expected value|
|------|------------|--------------|
|User provides a valid auction ID and valid bid amount|Valid bid placement|User is allowed to place a bid|
|User provides auction ID that does not exist and valid bid amount|Invalid bid placement|User is not allowed to place a bid|
|User provides a valid bet and the auction has started already|Valid bid placement|User is allowed to place a bid|
|User provides a valid bet, but the auction has not started yet|Invalid bid placement|User is not allowed to place a bid|
|User provides a valid bet and the auction time has not finished|Valid bid placement|User is allowed to place a bid|
|User provides a valid bet, but the auction time has finished|Invalid bid placement|User is not allowed to place a bid|
|User provides a number which is bigger than the current bid|Valid placement of a bid|User is allowed to place a bid|
|User provides a number which is smaller than the current bid|Invalid bid placement|User is not allowed to place a bid|


### Title: AT-ReceiveItem
|Input:|Description:|Expected value|
|------|------------|--------------|
| Buyer confirms the auction and makes payment| Buyer confirms that they won the auction and they make the payment to the bank to be held | Purchase confirmed |
|Seller ships the item to the buyer |Buyer and the seller agrees on the amount and the delivery type, then they must ship the item within the given period. | Auction Confirmed / Item Shipped | 
|Buyer receives the item and confirms the payment. | Buyer confirms they got the item, and then they confirm the money to be sent to the seller. The auction closes. | Buyer Received Item / Auction Closed |
|Buyer does not confirm the auction they won| Buyer either rejects or does not confirm the auction in time, so the auction is cancelled. Seller then can open the auction again.|Buyer did not confirm / Auction Cancelled|
|Seller does not ship the item in the agreed time|Seller does not ship the item in the agreed time, thus breaking the rules of agreement. The payment is now cancelled|The item was not shipped in time / Auction cancelled|
|Buyer does not receive or receive the item in bad condition| Buyer contacts the seller about the status of the item, and they try to settle the situation| Buyer did not receive or received the item in bad shape. / Auction Cancelled|

## Title: AT-SubscribeForAuction
|Input:|Description:|Expected value|
|------|------------|--------------|
|User pushes the subscribe button on the auction page they want to subscribe to | User tries to subscribe to an auction they want to follow | User subscribes to the auction |
|User already subscribed to the auction| User is trying to subscribe to the same auction more than once|User cannot subscribe to the auction|
|User banned from the auction|User is banned from subscribing to the auction|User cannot subscribe to the auction|

## Title: AT-Register
|Input:|Description:|Expected value|
|------|------------|--------------|
|User provides a string of characters != null & 0 < length <= 10 which only contains letters and numbers | A valid nickname| User is allowed to register using their provided nickname| 
|Password is a string of characters != null & length >= 8 and must contain one upper case, one number and one special character such as:!, @, #, $, %, ^, &, *| A valid password | User is allowed to register using their provided password| 
|User provides a string of characters != null & 0 < length <= 30 which contains only uppercase/lowercase letters. | A valid input of name/surname| User is allowed to register using their provided name / surname| 
| The user must enter a string that at most 50 characters long. All unicode characters accepted.| A valid address|  User is allowed to register using their provided address|
|User provides a string of characters != null & 0 < length <= 14 which contains only the '+' sign and numbers| A valid phone number | User is allowed to register using their provided phone number|
|User provides a string of characters != null & 0 < length <= 30 formatted in this way: prefix@domain. Prefix must be a string which contains only lettters and numbers. Domain must be in a format of a domain| A valid email| User is allowed to register using their provided email|  
|Input that does not correspond to the above cases|Any error in the above fields| The registration fails and the user must correct their inputs and try again.| 

## Title: AT-BecomeSeller
|Input:|Description:|Expected value|
|------|------------|--------------|
|User must provide their SSN as a string of characters != null & length > 0 which only contains numbers and letters| A valid SSN |The SSN the user has provided is confirmed and checked for no ToS breaks or blacklists and the user can become a seller.|
|User does not follow the SSN rules, or the entered SSN is not valid / blacklisted|Wrong / Non-valid / Blacklisted SSN|The SSN is rejected and the user cannot become a seller|

