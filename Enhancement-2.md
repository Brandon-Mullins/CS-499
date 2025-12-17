# Enhancement 2: Algorithms and Data Structures

# Briefly describe the artifact. What is it? When was it created?

The artifact used for this enhancement is the Weight Tracker Android application originally created during my CS 360 Mobile Architecture and Programming course. The original version of the application allowed users to record, update, and delete daily weight entries, but it primarily functioned as a basic data entry and storage tool without meaningful analysis of the data being collected.

For this enhancement, the application was expanded to include structured data analysis logic that evaluates weight entries over time. Specifically, the enhancement introduces weekly grouping and comparison logic that calculates average weight values for the current and previous weeks. This allows the application to move beyond simple record keeping and instead provide users with insights into their progress trends.

# Justify the inclusion of the artifact in your ePortfolio. Why did you select this item? What specific components of the artifact showcase your skills and abilities in algorithms and data structure? How was the artifact improved?

I selected this artifact because it provides a strong opportunity to demonstrate algorithmic thinking using real-world data. Rather than creating a standalone algorithm example, this enhancement integrates algorithms directly into a working application, which better reflects how algorithms and data structures are used in professional software development.

This enhancement showcases my ability to:

Group and filter structured data based on time ranges

Apply averaging and comparison algorithms to datasets

Use appropriate data structures to organize and process historical records

Separate computation logic from the UI layer for maintainability

The application was improved by adding a weekly trend analysis feature that computes the average weight for the current week and compares it to the previous week. Based on the calculated difference, the app provides meaningful feedback to the user, such as whether weight is trending downward, upward, or remaining stable. This turns raw data into actionable information and significantly improves the value of the application.

# Did you meet the course outcomes you planned to meet with this enhancement in Module One? Do you have any updates to your outcome-coverage plans?

Yes, this enhancement met the course outcomes I planned to address related to algorithms and data structures. I demonstrated the ability to design and evaluate computing solutions using algorithmic principles while managing trade-offs between simplicity, performance, and readability.

The weekly analysis logic required careful consideration of date ranges, dataset filtering, and edge cases such as missing or incomplete data. This enhancement strengthened my confidence in applying algorithmic reasoning to real application scenarios. With this enhancement completed, my remaining focus shifted to database persistence and long-term data reliability in the final enhancement.

# Reflect on the process of enhancing and modifying the artifact. What did you learn as you were creating it and improving it? What challenges did you face?

Through this enhancement, I learned how important it is to structure algorithms around real user needs rather than abstract problems. One of the main challenges was ensuring that the logic correctly handled time-based data, including determining the start of the week and comparing entries across different periods.

Another challenge involved handling situations where insufficient data existed for comparison. This required designing logic that was both accurate and user-friendly, providing clear feedback without producing misleading results. This process reinforced the importance of defensive programming and thoughtful data validation.

Overall, this enhancement strengthened my understanding of how algorithms and data structures directly contribute to user experience and decision making. It also reinforced how clean separation between data processing and UI logic leads to more maintainable and scalable software.
