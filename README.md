# Nimble
UGAHacks Spring 2021 submission

## Inspiration
Many apps exist that deal with making customers' lives easier during COVID and even non-pandemic times, so this time we wanted to focus on the staff, specifically the ones that interact with customers most: cashiers. Cashiers have a hard job, and they can help make or break a customer’s experience. So, to help everyone out, we made an app to help cashiers perform as best they can.

## What it does
Nimble provides a next-gen checkout system that allows merchants to use a phone’s native QR code reader to scan items from a catalog. Our solution cuts down on equipment prices for cashiers, while also allowing us to more effectively direct managerial support. By calculating a common performance measure, IPM (items per minute), our app automatically notifies a given manager through SMS when the cashier or employee falls below a certain threshold - indicating they might need help. This optimizes the manager’s time and alleviates the stress of cashiers needing to leave their post to contact the manager.

## How we built it
We used the NCR BSP Catalog API to store data about items and their prices, and used this to represent the inventory of a store. We braved the perilous jungles of Android Studio to build the app itself, using OkHTTP to send requests to the API and Gson to parse them within the app itself. We also used SQLite and its wrapper, Room, to allow the app to store and load data and Android’s SMS Manager to send text messages. To scan the QR codes themselves, we used the open-source project ZXing. In addition, the UI designing components are from Material Design and developed from scratch in XML, so the quality of the build is top-notch.

## Challenges we ran into
Most of us have little to no experience of Android App Development, so jumping the initial hurdles of understanding layout formatting and the activity life cycle was thoroughly challenging. After setting up the basics of the app itself, we simply weren’t satisfied, so we wanted to go above and beyond with the UI design, which in itself was very time-consuming. Furthermore, setting up the HTTP requests between the NCR Catalog API and Android Studio proved to be rather difficult, as well as implementing asynchronous tasks to keep track of the IPM (Items Per Minute) of a cashier. Finally, as we began implementing functionality through Java within Android Studio, learning the nitty-gritty details of proper app state management and activity execution truly tested our ability to persevere as we designed and implemented this project.

## Accomplishments that we're proud of
A big thing we’ve struggled with in previous hackathons was getting all of our features to work together after we built and tested them separately, so we're proud that we were able to integrate everything we wanted to without making a lot of sacrifices. It was our first time using a service like BSP, so we were also proud of figuring out how to use that as well. And we think our app looks pretty cool.

## What we learned
We definitely learned a ton about Android Development and how to fit all the pieces together. We also learned about making HTTP Requests in various programming languages (Python and Java) and how to parse JSON easily. Seeing the convenience of Room as an embedded database compared to conventional client-server models was eye-opening to say the least. It was also our first time using a sandbox environment like Postman, so we were able to get some good experience with testing custom APIs too.

## What's next for Nimble
The potential for Nimble is huge, considering efficient contactless checkout is highly-needed during the times of the pandemic. We plan to continue to work on improving Nimble, where the app will begin implementing authorized transactions and provide from a larger catalog of store-approved items. Furthermore, we plan to reach out to local businesses to run a prototypical version of our software to minimize costs and improve merchant efficiency in the workplace.
