## CrawlDroid 
###  Introduction
CrawlDroid is a toolset for GUI Testing of Android Applications.  it designs a novel feedback-based exploration strategy to reduce the cost of testing time, and  leverages manual-encoded tests to mine some domain knowledge about apps to improve code coverage. In order to  evaluate the effectiveness of the technique, we conduct some  empirical studies on 46 open source applications and 12 commercial applications. The numerical result is shown in the table.
 

| Subject       | BFS-states   |  BFS-EG  |BFS-AC|BFS-MT| F-states   | F-EG  |F-AC|F-MT|
| --------   | -----:  | :----:  |
|WordPress           |13|72|9|6              |28|60|9|15   
|MyExpenses         |44|151|23|7            |54|200|67|10 
|Sanity                   |1|5|3|6               |32|69|86|46   
|OSChina               |58|189|19|25          |88|223|45|40    
|AnyMemo 	        |30|115|15|10          |72|169|66|15   
|DalvikExplorer      |36|235|43|51        |22|68|69|80    
|NRPNews		        |49|254|30|5            |64|38|77|6	     
|BookCatalogue     |61|190|34|18		    |78|74|54|25    
|Tomdroid	             |34|177|50|31         |36|79|63|34	   
|Tipitaka                  |17|115|36|78            |66|158|73|90     
|ShoppingList          |1|6|33|4               |3|4|66|13 
|Blokish		             |13|48|33|41            |12|7|100|55	 
|Mileage		              |39|131|14|28         |42|242|42|33    
|LogicalDefence      |11|94|100|13         |11|41|100|14	   
|PasswordMaker     |5|23|33|41             |9|18|66|54     
|Whohasmystuff       |11|135|100|58      |18|83 |100|91    
|WorldClock	          |14 |134|50|79      |23|194|50|80	   
|OpenManga	          |46|206| 35|14        |86|56|57|35     
|FileExplorer	         |23| 142|50|30          |42|230|50|42           
|Ultramegatech	      |13|197|50|60         |15|24|50|63       
|Omnomagon	   	      |	17|126|100|14	      |30|73|75|34  
|ALogcat	               |13|153 |100|73       |16|126|100|74  
|Feeder		              |34|193|45|31             |47|284|55|64    
|BatteryManager      |4|19|100|71            |7|4|100|71      
|Yahtzee	               |3 |35|100|43	         |5|5|100|47        
|AGrep                    |25|137|66|49	           |18|150|83|58  
|Mirrored	              |14|138|75|76            |28|155|100|82  
|BatteryDog             |3|19|100|87           |5|17|100|89  
|Addi	                      |8 |27|50|9     	        |13 |9|50|11   
|CrimeTalk              |28|162|100|41	       |31|159|100|41	  
|A2DP 		              |23|175|50|60            |27|73|100|63  
|Democracydroid    |29|200|80|29          |22|123|100|37   
|Autoanswer            |3|24| 100|15          |3|14|100|15  
|Zooborns               |5|20|50| 23             |5|11|50|23   
|LearnMusicNotes   |15|106|100|57          |14|176|100|62  
|DdalyHeart              |8|37|75|9           |11|49|75|12 
|Chronosnap            |7|53|100|31            |9|35|100|45 
|ImportContacts        |19|100|100|37       |20|65|100|39 
|NetCounter             |13|56|66|55             |3|14|66|57 
|MiniNoteViewer       |6|6|12|2              |57|320|12|57 
|AnyCut                    |14|100|100|72         |15|67|100|77 
|CountDownTimer     |1|4|100|26          |3|18|100|70 
|Multismssender       |15|102|66|52           |17|22|33|68 
|Alarmclock              |25|180|60|5              |28|123|60|64 
||
|Dotools Clock     |16|145|17|16                 |31|121|40|29 
|AudioClass          |10|53|25|4                  |10|53|25|4 
|WeatherBug         |39|146|11|30                 |46|134|17|30  
|Cdxc                    |7|24|11|2                      |16|24|28|9 
|TED                     |18|152|8|33                 |28|153|28|35  
|CNN mobile          |14|130|9|13                  |34|120|36|28  
|Ergedd                 |29|223|17|24               |43|221|25|26  
|Smartisan Notes  |15|164|16|14                |26|175|30|17 
|Jams Music          |30|103|25|5                  |71|108|56|29 
|Qukan                  |41|217|8|18                   |55|216|17|20 
|Flixster                 |22|190|8|31                   |62|196|31|44 
|Qiyouyuedu         |26|178|25|19                |46|194|33|36 

The following sections show some  pictures and video of CrawlDroid.
**record test scripts**
![](https://img.shields.io/github/stars/pandao/editor.md.svg) ![
**run CrawlDroid**
(https://img.shields.io/github/forks/pandao/editor.md.svg) ![]
**collect test result**
![](https://pandao.github.io/editor.md/images/logos/editormd-logo-180x180.png)
### Contact
    sheyi14@otcaix.iscas.ac.cn
