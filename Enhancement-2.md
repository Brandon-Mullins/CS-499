Brandon Mullins
CS-499
Module Four: Milestone Three
Southern New Hampshire University

 
Briefly describe the artifact. What is it? When was it created?
The artifact I selected for my algorithms and data structures enhancement is the Weekly Trend Summary feature inside my Weight Tracker Android app. This feature was added during my CS 360 coursework, but the original version of the app did not include any algorithmic analysis. The app originally allowed users to add, update, and delete weight entries, and it displayed them in a basic list.
For this enhancement, I added a full data-processing algorithm that analyzes weight entries across two different weeks. The code calculates this week's average weight, last week's average weight, and then produces an interpretation of the user's progress. This logic is implemented in a new function called calculateWeeklyTrend(), along with a small data class used to store the results.
Justify the inclusion of the artifact in your ePortfolio. Why did you select this item? What specific components of the artifact showcase your skills and abilities in algorithms and data structure? How was the artifact improved?
I included this artifact in my ePortfolio because it clearly demonstrates my ability to design and implement an algorithm that processes collections of data and produces meaningful insights. This enhancement required me to work directly with lists, filtering, date ranges, averaging, and conditional logic. It shows that I can apply computer science fundamentals to solve real-world problems.
The Weekly Trend Summary transforms the app from a basic CRUD tracker into something that gives users useful feedback based on their data. The improvement includes:

– A custom algorithm that groups entries by week
– Logic for calculating weekly averages
– Conditional analysis that determines whether the user is improving, declining, or maintaining
– A structured data model to return results cleanly
– Integration of the computed results into the UI
This enhancement showcases my ability to design lightweight data structures, build efficient logic over lists, and implement algorithms that add real value to an application.

Did you meet the course outcomes you planned to meet with this enhancement in Module One? Do you have any updates to your outcome-coverage plans?
Yes, this enhancement directly meets the course outcome related to algorithms and data structures. I was able to design and evaluate a computing solution using algorithmic principles, which was the goal I set in Module One. The enhancement required careful thought about how to group data, compute results efficiently, and design a clear structure for returning those results.
This enhancement also helped reinforce the outcome about delivering professional-quality improvements because I had to integrate the algorithm cleanly into the UI and ensure that the results updated automatically as the dataset changed. For future enhancements, I will continue focusing on data processing and possibly add more trend analysis, such as monthly summaries or progress predictions.

Reflect on the process of enhancing and modifying the artifact. What did you learn as you were creating it and improving it? What challenges did you face?
Enhancing this artifact taught me a lot about how important data analysis can be in even simple applications. One challenge was handling date ranges correctly, specially making sure that entries were grouped into the correct weeks. I had to work with the Java Time library and be precise with how I calculated the start and end of weeks.
I also had to consider cases where users might not have enough data for comparison, which required using nullable values and guard clauses. Another challenge was ensuring that the UI updated correctly whenever new entries were added or modified, since the algorithm needed to refresh anytime the list changed.
Overall, this enhancement improved both the technical logic of the app and my understanding of how algorithms support meaningful features. It strengthened my ability to build structured, maintainable logic that transforms raw data into actionable insights for users.
