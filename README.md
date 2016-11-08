# Codepath 2016 Fall Cohort - *Bookd*

**Bookd** is an Android app that makes planning events easier.  

Backstory: When recently planning a wedding without the services of a wedding planner, I saw how daunting the process could be. 

For example, to find a photographer that can shoot the event: Go through a web search to find photographers. Go through their website to see examples of their work. Call them to see their availability and pricing. Repeat for a handful of photographers and hope that you can decide on one after all of your research.

Now imagine doing the exact same process to find videographers, decorators, cake makers, makeup artists, hairstylers, performers, etc. - all through web searches, Craigslist, and Kijiji. This becomes a daunting task that can be made a lot simpler. 

In comes *Bookd*, matching those in the one time gig industry looking to get hired for their work with event planners looking to find the right person to get the job done.


## User Stories

The following **required** functionality is completed:

*Mobile-oriented features*
* [] Location is used to find gig providers that are in drivable distance
* [] Push notifications provide notice of confirmed gigs

*Search*
* [] Search bar to look up specific gigs/businesses
 * [] Filter searches by category, rating, price range, event-day availability, response time, sort options
* [] Card views of search results showing an image, business/gig name, star rating, and price range

*Business Profile*
* [] View business profile
 * [] User can view business name, photo, rating, price range, links to social media webpages/website for examples of work
 * [] User can view a date dialog to see event date availability 

*Book for event*
* [] Users can confirm a booking with a business (finalize price, destination, # of hrs, etc.)
 * [] Uses a payment API (Stripe, Square, etc) to process payments between event planner and business
 
The following **optional** features are implemented:

*View Bookings*
* [] User can view upcoming planned events: 
	* [] Show finalized and pending bookings for those days
	* [] Provide user with notifications to complete bookings 	

*Gig-provider side for the app (Business usage)*
* [] Gig provider can update their profile information through the app
	* [] Availability
	* [] Accept and decline work
	* [] Provide quotes to event planners
	* [] Pay to be the sponsored gig-provider of the day


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />


## Notes


## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright [2016] [Ilyusha, Rubab, and Pranav]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.