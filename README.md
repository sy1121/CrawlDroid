## CrawlDroid 
###  Introduction
CrawlDroid is a toolset for GUI Testing of Android Applications.  it designs a novel feedback-based exploration strategy to reduce the cost of testing time, and  leverages manual-encoded tests to mine some domain knowledge about apps to improve code coverage. In order to  evaluate the effectiveness of the technique, we conduct some  empirical studies on 46 open source applications and 12 commercial applications. The numerical result is shown in the table.
 
###  result of empirical studies
<table border="0" style="font-size: 8px">
   <tr>
      <td>Subject</td>
      <td colspan="5" align="center">BFS</td>
      <td  colspan="5" align="center">Feedback</td>
      <td  colspan="3" align="center">Monkey</td>
   </tr>
   <tr>
      <td></td>
      <td>states</td>
      <td>EG</td>
      <td>AC</td>
      <td>MT</td>
      <td>SC</td>
      <td>states</td>
      <td>EG</td>
      <td>AC</td>
      <td>MT</td>
      <td>SC</td>
      <td>AC</td>
      <td>MT</td>
      <td>SC</td>
   </tr>
   <tr>
      <td>WordPress         </td>
      <td>13</td>
      <td>72</td>
      <td>9</td>
      <td>6</td>
      <td>2</td>
      <td>28</td>
      <td>60</td>
      <td>9</td>
      <td>15</td>
      <td>3</td>
      <td>9</td>
      <td>23</td>
      <td></td>
   </tr>
   <tr>
      <td>MyExpenses      </td>
      <td>44</td>
      <td>151</td>
      <td>23</td>
      <td>7</td>
      <td></td>
      <td>54</td>
      <td>200</td>
      <td>67</td>
      <td>10</td>
      <td></td>
      <td>52</td>
      <td>15</td>
      <td></td>
   </tr>
   <tr>
      <td>Sanity              </td>
      <td>1</td>
      <td>5</td>
      <td>3</td>
      <td>6</td>
      <td>4</td>
      <td>32</td>
      <td>69</td>
      <td>86</td>
      <td>46</td>
      <td>18</td>
      <td>71</td>
      <td>42</td>
      <td></td>
   </tr>
   <tr>
      <td>OSChina             </td>
      <td>58</td>
      <td>189</td>
      <td>19</td>
      <td>25</td>
      <td>19</td>
      <td>88</td>
      <td>223</td>
      <td>45</td>
      <td>40</td>
      <td>34</td>
      <td>45</td>
      <td>33</td>
      <td></td>
   </tr>
   <tr>
      <td>AnyMemo </td>
      <td>30</td>
      <td>115</td>
      <td>15</td>
      <td>10</td>
      <td>20</td>
      <td>72</td>
      <td>169</td>
      <td>66</td>
      <td>15</td>
      <td>39</td>
      <td>48</td>
      <td>15</td>
      <td></td>
   </tr>
   <tr>
      <td>DalvikExplorer    </td>
      <td>36</td>
      <td>235</td>
      <td>43</td>
      <td>51</td>
      <td>30</td>
      <td>22</td>
      <td>68</td>
      <td>69</td>
      <td>80</td>
      <td>65</td>
      <td>94</td>
      <td>79</td>
      <td></td>
   </tr>
   <tr>
      <td>NRPNews</td>
      <td>49</td>
      <td>254</td>
      <td>30</td>
      <td>5</td>
      <td></td>
      <td>64</td>
      <td>38</td>
      <td>77</td>
      <td>6</td>
      <td></td>
      <td>77</td>
      <td>5</td>
      <td></td>
   </tr>
   <tr>
      <td>BookCatalogue    </td>
      <td>61</td>
      <td>190</td>
      <td>34</td>
      <td>18</td>
      <td>10</td>
      <td>78</td>
      <td>74</td>
      <td>54</td>
      <td>25</td>
      <td>3</td>
      <td>54</td>
      <td>28</td>
      <td></td>
   </tr>
   <tr>
      <td>Tomdroid</td>
      <td>34</td>
      <td>177</td>
      <td>50</td>
      <td>31</td>
      <td>26</td>
      <td>36</td>
      <td>79</td>
      <td>63</td>
      <td>34</td>
      <td>28</td>
      <td>63</td>
      <td>43</td>
      <td></td>
   </tr>
   <tr>
      <td>Tipitaka              </td>
      <td>17</td>
      <td>115</td>
      <td>36</td>
      <td>78</td>
      <td></td>
      <td>66</td>
      <td>158</td>
      <td>73</td>
      <td>90</td>
      <td></td>
      <td>73</td>
      <td>9</td>
      <td></td>
   </tr>
   <tr>
      <td>ShoppingList          </td>
      <td>1</td>
      <td>6</td>
      <td>33</td>
      <td>4</td>
      <td></td>
      <td>3</td>
      <td>4</td>
      <td>66</td>
      <td>13</td>
      <td></td>
      <td>67</td>
      <td>14</td>
      <td></td>
   </tr>
   <tr>
      <td>Blokish</td>
      <td>13</td>
      <td>48</td>
      <td>33</td>
      <td>41</td>
      <td>1</td>
      <td>12</td>
      <td>7</td>
      <td>100</td>
      <td>55</td>
      <td>1</td>
      <td>67</td>
      <td>43</td>
      <td></td>
   </tr>
   <tr>
      <td>Mileage</td>
      <td>39</td>
      <td>131</td>
      <td>14</td>
      <td>28</td>
      <td>25</td>
      <td>42</td>
      <td>242</td>
      <td>42</td>
      <td>33</td>
      <td>28</td>
      <td>42</td>
      <td>44</td>
      <td></td>
   </tr>
   <tr>
      <td>LogicalDefence     </td>
      <td>11</td>
      <td>94</td>
      <td>100</td>
      <td>13</td>
      <td></td>
      <td>11</td>
      <td>41</td>
      <td>100</td>
      <td>14</td>
      <td></td>
      <td>100</td>
      <td>14</td>
      <td></td>
   </tr>
   <tr>
      <td>PasswordMaker     </td>
      <td>5</td>
      <td>23</td>
      <td>33</td>
      <td>41</td>
      <td>33</td>
      <td>9</td>
      <td>18</td>
      <td>66</td>
      <td>54</td>
      <td>29</td>
      <td>67</td>
      <td>75</td>
      <td></td>
   </tr>
   <tr>
      <td>Whohasmystuff      </td>
      <td>11</td>
      <td>135</td>
      <td>100</td>
      <td>58</td>
      <td>2</td>
      <td>18</td>
      <td>83</td>
      <td>100</td>
      <td>91</td>
      <td>2</td>
      <td>100</td>
      <td>82</td>
      <td></td>
   </tr>
   <tr>
      <td>WorldClock</td>
      <td>14</td>
      <td>134</td>
      <td>50</td>
      <td>79</td>
      <td>90</td>
      <td>23</td>
      <td>194</td>
      <td>50</td>
      <td>80</td>
      <td>88</td>
      <td>50</td>
      <td>80</td>
      <td></td>
   </tr>
   <tr>
      <td>OpenManga</td>
      <td>46</td>
      <td>206</td>
      <td>35</td>
      <td>14</td>
      <td></td>
      <td>86</td>
      <td>56</td>
      <td>57</td>
      <td>35</td>
      <td></td>
      <td>66</td>
      <td>42</td>
      <td></td>
   </tr>
   <tr>
      <td>FileExplorer</td>
      <td>23</td>
      <td>142</td>
      <td>50</td>
      <td>30</td>
      <td>24</td>
      <td>42</td>
      <td>230</td>
      <td>50</td>
      <td>42</td>
      <td>34</td>
      <td>50</td>
      <td>35</td>
      <td></td>
   </tr>
   <tr>
      <td>Ultramegatech</td>
      <td>13</td>
      <td>197</td>
      <td>50</td>
      <td>60</td>
      <td>1</td>
      <td>15</td>
      <td>24</td>
      <td>50</td>
      <td>63</td>
      <td>1</td>
      <td>50</td>
      <td>64</td>
      <td></td>
   </tr>
   <tr>
      <td>Omnomagon</td>
      <td>17</td>
      <td>126</td>
      <td>100</td>
      <td>14</td>
      <td></td>
      <td>30</td>
      <td>73</td>
      <td>75</td>
      <td>34</td>
      <td></td>
      <td>75</td>
      <td>40</td>
      <td></td>
   </tr>
   <tr>
      <td>ALogcat</td>
      <td>13</td>
      <td>153</td>
      <td>100</td>
      <td>73</td>
      <td></td>
      <td>16</td>
      <td>126</td>
      <td>100</td>
      <td>74</td>
      <td></td>
      <td>100</td>
      <td>74</td>
      <td></td>
   </tr>
   <tr>
      <td>Feeder</td>
      <td>34</td>
      <td>193</td>
      <td>45</td>
      <td>31</td>
      <td></td>
      <td>47</td>
      <td>284</td>
      <td>55</td>
      <td>64</td>
      <td></td>
      <td>45</td>
      <td>57</td>
      <td></td>
   </tr>
   <tr>
      <td>BatteryManager     </td>
      <td>4</td>
      <td>19</td>
      <td>100</td>
      <td>71</td>
      <td>51</td>
      <td>7</td>
      <td>4</td>
      <td>100</td>
      <td>71</td>
      <td>82</td>
      <td>100</td>
      <td>71</td>
      <td></td>
   </tr>
   <tr>
      <td>Yahtzee</td>
      <td>3</td>
      <td>35</td>
      <td>100</td>
      <td>43</td>
      <td>24</td>
      <td>5</td>
      <td>5</td>
      <td>100</td>
      <td>47</td>
      <td>6</td>
      <td>50</td>
      <td>5</td>
      <td></td>
   </tr>
   <tr>
      <td>AGrep                   </td>
      <td>25</td>
      <td>137</td>
      <td>66</td>
      <td>49</td>
      <td>30</td>
      <td>18</td>
      <td>150</td>
      <td>83</td>
      <td>58</td>
      <td>38</td>
      <td>63</td>
      <td>48</td>
      <td></td>
   </tr>
   <tr>
      <td>Mirrored</td>
      <td>14</td>
      <td>138</td>
      <td>75</td>
      <td>76</td>
      <td>2</td>
      <td>28</td>
      <td>155</td>
      <td>100</td>
      <td>82</td>
      <td>2</td>
      <td>100</td>
      <td>81</td>
      <td></td>
   </tr>
   <tr>
      <td>BatteryDog           </td>
      <td>3</td>
      <td>19</td>
      <td>100</td>
      <td>87</td>
      <td>58</td>
      <td>5</td>
      <td>17</td>
      <td>100</td>
      <td>89</td>
      <td>13</td>
      <td>100</td>
      <td>90</td>
      <td></td>
   </tr>
   <tr>
      <td>Addi</td>
      <td>8</td>
      <td>27</td>
      <td>50</td>
      <td>9</td>
      <td>17</td>
      <td>13</td>
      <td>9</td>
      <td>50</td>
      <td>11</td>
      <td>17</td>
      <td>50</td>
      <td>11</td>
      <td></td>
   </tr>
   <tr>
      <td>CrimeTalk             </td>
      <td>28</td>
      <td>162</td>
      <td>100</td>
      <td>41</td>
      <td></td>
      <td>31</td>
      <td>159</td>
      <td>100</td>
      <td>41</td>
      <td></td>
      <td>100</td>
      <td>41</td>
      <td></td>
   </tr>
   <tr>
      <td>A2DP </td>
      <td>23</td>
      <td>175</td>
      <td>50</td>
      <td>60</td>
      <td>0</td>
      <td>27</td>
      <td>73</td>
      <td>100</td>
      <td>63</td>
      <td>0</td>
      <td>100</td>
      <td>63</td>
      <td></td>
   </tr>
   <tr>
      <td>Democracydroid   </td>
      <td>29</td>
      <td>200</td>
      <td>80</td>
      <td>29</td>
      <td></td>
      <td>22</td>
      <td>123</td>
      <td>100</td>
      <td>37</td>
      <td></td>
      <td>100</td>
      <td>38</td>
      <td></td>
   </tr>
   <tr>
      <td>Autoanswer          </td>
      <td>3</td>
      <td>24</td>
      <td>100</td>
      <td>15</td>
      <td>14</td>
      <td>3</td>
      <td>14</td>
      <td>100</td>
      <td>15</td>
      <td>27</td>
      <td>100</td>
      <td>15</td>
      <td></td>
   </tr>
   <tr>
      <td>Zooborns            </td>
      <td>5</td>
      <td>20</td>
      <td>50</td>
      <td>23</td>
      <td>16</td>
      <td>5</td>
      <td>11</td>
      <td>50</td>
      <td>23</td>
      <td>30</td>
      <td>50</td>
      <td>24</td>
      <td></td>
   </tr>
   <tr>
      <td>LearnMusicNotes </td>
      <td>15</td>
      <td>106</td>
      <td>100</td>
      <td>57</td>
      <td>30</td>
      <td>14</td>
      <td>176</td>
      <td>100</td>
      <td>62</td>
      <td>34</td>
      <td>100</td>
      <td>42</td>
      <td></td>
   </tr>
   <tr>
      <td>DdalyHeart            </td>
      <td>8</td>
      <td>37</td>
      <td>75</td>
      <td>9</td>
      <td></td>
      <td>11</td>
      <td>49</td>
      <td>75</td>
      <td>12</td>
      <td></td>
      <td>75</td>
      <td>11</td>
      <td></td>
   </tr>
   <tr>
      <td>Chronosnap          </td>
      <td>7</td>
      <td>53</td>
      <td>100</td>
      <td>31</td>
      <td>7</td>
      <td>9</td>
      <td>35</td>
      <td>100</td>
      <td>45</td>
      <td>7</td>
      <td>100</td>
      <td>45</td>
      <td></td>
   </tr>
   <tr>
      <td>ImportContacts      </td>
      <td>19</td>
      <td>100</td>
      <td>100</td>
      <td>37</td>
      <td>26</td>
      <td>20</td>
      <td>65</td>
      <td>100</td>
      <td>39</td>
      <td>28</td>
      <td>100</td>
      <td>40</td>
      <td></td>
   </tr>
   <tr>
      <td>NetCounter           </td>
      <td>13</td>
      <td>56</td>
      <td>66</td>
      <td>55</td>
      <td>40</td>
      <td>3</td>
      <td>14</td>
      <td>66</td>
      <td>57</td>
      <td>37</td>
      <td>67</td>
      <td>57</td>
      <td></td>
   </tr>
   <tr>
      <td>MiniNoteViewer      </td>
      <td>6</td>
      <td>6</td>
      <td>12</td>
      <td>2</td>
      <td>15</td>
      <td>57</td>
      <td>320</td>
      <td>12</td>
      <td>57</td>
      <td>30</td>
      <td>13</td>
      <td>57</td>
      <td></td>
   </tr>
   <tr>
      <td>AnyCut                </td>
      <td>14</td>
      <td>100</td>
      <td>100</td>
      <td>72</td>
      <td>49</td>
      <td>15</td>
      <td>67</td>
      <td>100</td>
      <td>77</td>
      <td>67</td>
      <td>100</td>
      <td>80</td>
      <td></td>
   </tr>
   <tr>
      <td>CountDownTimer   </td>
      <td>1</td>
      <td>4</td>
      <td>100</td>
      <td>26</td>
      <td>4</td>
      <td>3</td>
      <td>18</td>
      <td>100</td>
      <td>70</td>
      <td>11</td>
      <td>100</td>
      <td>70</td>
      <td></td>
   </tr>
   <tr>
      <td>Multismssender     </td>
      <td>15</td>
      <td>102</td>
      <td>66</td>
      <td>52</td>
      <td>33</td>
      <td>17</td>
      <td>22</td>
      <td>33</td>
      <td>68</td>
      <td>40</td>
      <td>33</td>
      <td>45</td>
      <td></td>
   </tr>
   <tr>
      <td>Alarmclock           </td>
      <td>25</td>
      <td>180</td>
      <td>60</td>
      <td>5</td>
      <td></td>
      <td>28</td>
      <td>123</td>
      <td>60</td>
      <td>64</td>
      <td></td>
      <td>60</td>
      <td>64</td>
      <td></td>
   </tr>
   <tr>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
   </tr>
   <tr>
      <td>Dotools Clock   </td>
      <td>16</td>
      <td>145</td>
      <td>17</td>
      <td>16</td>
      <td></td>
      <td>31</td>
      <td>121</td>
      <td>40</td>
      <td>29</td>
      <td></td>
      <td>37</td>
      <td>29</td>
      <td></td>
   </tr>
   <tr>
      <td>AudioClass        </td>
      <td>10</td>
      <td>53</td>
      <td>25</td>
      <td>4</td>
      <td></td>
      <td>10</td>
      <td>53</td>
      <td>25</td>
      <td>4</td>
      <td></td>
      <td>25</td>
      <td>7</td>
      <td></td>
   </tr>
   <tr>
      <td>WeatherBug       </td>
      <td>39</td>
      <td>146</td>
      <td>11</td>
      <td>30</td>
      <td></td>
      <td>46</td>
      <td>134</td>
      <td>17</td>
      <td>30</td>
      <td></td>
      <td>22</td>
      <td>32</td>
      <td></td>
   </tr>
   <tr>
      <td>Cdxc                </td>
      <td>7</td>
      <td>24</td>
      <td>11</td>
      <td>2</td>
      <td></td>
      <td>16</td>
      <td>24</td>
      <td>28</td>
      <td>9</td>
      <td></td>
      <td>22</td>
      <td>8</td>
      <td></td>
   </tr>
   <tr>
      <td>TED                  </td>
      <td>18</td>
      <td>152</td>
      <td>8</td>
      <td>33</td>
      <td></td>
      <td>28</td>
      <td>153</td>
      <td>28</td>
      <td>35</td>
      <td></td>
      <td>24</td>
      <td>35</td>
      <td></td>
   </tr>
   <tr>
      <td>CNN mobile        </td>
      <td>14</td>
      <td>130</td>
      <td>9</td>
      <td>13</td>
      <td></td>
      <td>34</td>
      <td>120</td>
      <td>36</td>
      <td>28</td>
      <td></td>
      <td>27</td>
      <td>25</td>
      <td></td>
   </tr>
   <tr>
      <td>Ergedd              </td>
      <td>29</td>
      <td>223</td>
      <td>17</td>
      <td>24</td>
      <td></td>
      <td>43</td>
      <td>221</td>
      <td>25</td>
      <td>26</td>
      <td></td>
      <td>33</td>
      <td>25</td>
      <td></td>
   </tr>
   <tr>
      <td>Smartisan Notes  </td>
      <td>15</td>
      <td>164</td>
      <td>16</td>
      <td>14</td>
      <td></td>
      <td>26</td>
      <td>175</td>
      <td>30</td>
      <td>17</td>
      <td></td>
      <td>23</td>
      <td>15</td>
      <td></td>
   </tr>
   <tr>
      <td>Jams Music         </td>
      <td>30</td>
      <td>103</td>
      <td>25</td>
      <td>5</td>
      <td></td>
      <td>71</td>
      <td>108</td>
      <td>56</td>
      <td>29</td>
      <td></td>
      <td>25</td>
      <td>6</td>
      <td></td>
   </tr>
   <tr>
      <td>Qukan                </td>
      <td>41</td>
      <td>217</td>
      <td>8</td>
      <td>18</td>
      <td></td>
      <td>55</td>
      <td>216</td>
      <td>17</td>
      <td>20</td>
      <td></td>
      <td>21</td>
      <td>21</td>
      <td></td>
   </tr>
   <tr>
      <td>Flixster              </td>
      <td>22</td>
      <td>190</td>
      <td>8</td>
      <td>31</td>
      <td></td>
      <td>62</td>
      <td>196</td>
      <td>31</td>
      <td>44</td>
      <td></td>
      <td>27</td>
      <td>37</td>
      <td></td>
   </tr>
   <tr>
     <td>Qiyouyuedu</td>
      <td>26</td>
      <td>178</td>
      <td>25</td>
      <td>19</td>
      <td></td>
      <td>46</td>
      <td>194</td>
      <td>33</td>
      <td>36</td>
      <td></td>
      <td>27</td>
      <td>37</td>
      <td></td>
   </tr>
</table>
The following sections show some  pictures and video of CrawlDroid.

**record test scripts**
dd
**run CrawlDroid**
dd
**collect test result**
dd
### Contact
sheyi14@otcaix.iscas.ac.cn
