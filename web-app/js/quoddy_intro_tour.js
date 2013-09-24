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
        	
        },
        {
            title: "Calendar Entry",
            content: "",
            target: ".calendarEntry",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "Neddick Link",
            content: "",
            target: ".neddickLink",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "RSS Feed Item",
            content: "",
            target: ".rssFeedItem",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "Business Event",
            content: "",
            target: ".businessSubscriptionEvent",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "Reshared Item",
            content: "",
            target: ".resharedActivityStreamEntry",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "Remote ActivityStream Item",
            content: "",
            target: ".remoteActivityStreamEntry",
            placement: "top",
            width: 350        	
        	
        },
        {
            title: "Activiti User Task",
            content: "",
            target: ".activitiUserTask",
            placement: "top",
            width: 350        	
        	
        }
      ]
    
    
      /* ,
      onStart: function() {
          alert( "onStart");
        },
       onError: function() {
    	   alert( "onError" );
       }
      */
    
    }