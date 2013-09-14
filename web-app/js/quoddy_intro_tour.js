// Define the tour!
    var tour = {
      id: "quoddy-intro",
      steps: [
        {
          title: "Quoddy Intro",
          content: "This is Quoddy",
          target: "TBD",
          placement: "bottom"
        },
        {
          title: "Step Two",
          content: "Step Two",
          target: document.querySelector("#content p"),
          placement: "bottom"
        }
      ]
    };

    // Start the tour!
    // alert( "starting tour!");
    // hopscotch.startTour(tour);