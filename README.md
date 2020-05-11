This repository contains a simple sample app that displays expected arrival time of Trams at specified stops using the Tram Tracker API

__**Architecture**__
==================
The app was implemented using the Google recommended MVVM and Android Architecture components.

The network layer is implemented using Retrofit and RxJava

The ViewModel is implemented with some Android Architecture components.

The view is very light weight as no business logic sits there. This makes extensibility, code maintenance and testing much easier


__**Details**__
- The app has one activity that contains a ViewPager.

- A List<Pair<StopName, stopId>> is used to store the required stop Ids

- onCreate, n TramsFragments are created and placed in the ViewPager.  n = size of List<Pair<StopName, stopId>>

- The MainActivity handles global events like initial request, refresh and listening for global events like errors and network connectivity

- Each Fragment is created by calling the TramsFragment.newInstance method which takes the stopId as param. The fragments also use the same ViewModel scoped to the MainActivity. 

- Each fragment is responsible for listening to the Trams LiveData response and filtering the list for it's own stopId.

- The list is the displayed in a RecyclerView.

- The RecyclerView displays a list of custom TramViews. All the operations and display logic of each row is encapsulated in this custom view.

- To avoid repeated network request while maintaining the appearance of real-time data, the TramView has a countdown TextView that starts from the supplied relative time and counts until zero (depicted by 'now')

- Also, to avoid discrepancy between the countdown and actual data(which will not stay true to actual clock), when the countdown reaches a specified threshold (2min at the moment), a refresh() is automatically called again. This will correct the stale data.



- The ViewModel is well unit tested. Success paths and Failure paths of each requests are tested

- The views are also uit tested using Robolectric



__**Choices**__
==================
- As this is one that requires near-realtime accuracy of data, I chose to refresh every time the app comes into focus (onResume()). This should save the user tapping the 'Refresh' button constantly.

- To avoid over-engineering, I chose not to have a repository layer at the moment for this simple data flow. For a more complex app, I would move part of the ViewModel to a repo layer to deal with, say, network layer, database interactions...

- In a bid to get a good UI/UX while keeping the use case flexible, I decided to use fragments in a ViewPager to display Trams for each stop. With the current design, I could make a request for 10 different stops and the UI is flexible enough to display the data in a usable way.




__**Assumptions**__
==================
- The Tram.PredictedArrivalDateTime in the API response will always be in the future



__**Known Issues**__
==================
- All test run when the test classes are run from the test folder, but some Robolectric tests fail when run with "Test with Coverage". This seems to be a known issue with Robolectric



__**Improvements**__
==================
- Implement a landscape layout and possibly another for very large devices like tablets (depending on use case)








__Tram Tracker API__

This app uses the same API as the Tram Tracker app, but it's not an officially public API so there is a chance it'll just stop working at some stage. It's more fun to use a real API though. To use the tram tracker API, you need to first connect with an endpoint that gives you an API token. That token can then be used for future calls.

To retrieve an API token, you hit this endpoint `http://ws3.tramtracker.com.au/TramTracker/RestService/GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeiOS` and retrieve the token from the response. The app id and dev info parameters have been coded in for you, as these should not need to change.

```
{
  errorMessage: null,
  hasError: false,
  hasResponse: true,
  responseObject: [
    {
      DeviceToken: "some-valid-device-token"
    }
  ]
}
```

We can then use this device token to retrieve the upcoming trams. The route ID and stop IDs that we pass to the API have been hard coded to represent the tram stops on either side of the road. The endpoint that retrieves the tram (with stop ID and token replaced with valid values) will be of the form `http://ws3.tramtracker.com.au/TramTracker/RestService/GetNextPredictedRoutesCollection/{STOP_ID}/78/false/?aid=TTIOSJSON&cid=2&tkn={TOKEN}`, returns the upcoming trams in the form:

```
{
  errorMessage: null,
  hasError: false,
  hasResponse: true,
  responseObject: [
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: ""/Date(1425407340000+1100)/"",
      RouteNo: "78"
    },
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: "/Date(1425408480000+1100)/",
      RouteNo: "78"
    },
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: "/Date(1425409740000+1100)/",
      RouteNo: "78"
    }
  ]
}
```
