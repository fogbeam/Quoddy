// Define the tour!
    var tour = {
      id: "quoddy-intro",
      steps: [
        {
          title: "Activity Stream",
          content: "This is the Activity Stream area.  Various kinds of activities appear here, based on the" +
          " users you follow, groups you join, subscriptions you setup, etc.  Activity Stream events include Status Updates" +
          " from other users, Calendar Feed entries, Business Events, RSS Feed entries, BPM User Task entries, and others.",
          target: "activityStream",
          placement: "top",
          width: 350
        },
        {
            title: "Status Update",
            content: "This is a Status Update, shared by one of your Friends or someone you Follow.",
            target: ".basicActivityStreamEntry",
            placement: "top",
            width: 350        	
        	
        }
      ],
      onStart: function() {
          alert( "onStart");
        },
       onError: function() {
    	   alert( "onError" );
       }
    }