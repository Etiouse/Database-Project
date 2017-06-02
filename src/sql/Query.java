package sql;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import tools.Button;
import tools.Mouse;

public class Query {

	private String name;
	private String description;
	private String sql;
	private Image img;
	private Image glow;
	private Image click;
	private Button button;
	private boolean clicked;
	private int x;
	private int y;

	private String[] descriptions = {
			"Print the brand group names with the highest number of Belgian indicia publishers", 
			"Print the ids and names of publishers of Danish book series",
			"Print the names of all Swiss series that have been published in magazines",
			"Starting from 1990, print the number of issues published each year",
			"Print the number of series for each indicia publisher whose name resembles ‘DC comics’",
			"Print the titles of the 10 most reprinted stories",
			"Print the artists that have scripted, drawn, and colored at least one of the stories they were involved in",
			"Print all non-reprinted stories involving Batman as a non-featured character",
			"Print the series names that have the highest number of issues which contain a story whose type (e.g., cartoon) is not the one occurring most frequently in the database",
			"Print the names of publishers who have series with all series types",
			"Print the 10 most-reprinted characters from Alan Moore's stories",
			"Print the writers of nature-related stories that have also done the pencilwork in all their nature-related stories.",
			"For each of the top-10 publishers in terms of published series, print the 3 most popular languages of their series",
			"Print the languages that have more than 10000 original stories published in magazines, along with the number of those stories",
			"Print all story types that have not been published as a part of Italian magazine series",
			"Print the writers of cartoon stories who have worked as writers for more than one indicia publisher",
			"Print the 10 brand groups with the highest number of indicia publishers",
			"Print the average series length (in terms of years) per indicia publisher",
			"Print the top 10 indicia publishers that have published the most single-issue series",
			"Print the 10 indicia publishers with the highest number of script writers in a single story.",
			"Print all Marvel heroes that appear in Marvel-DC story crossovers",
			"Print the top 5 series with most issues",
			"Given an issue, print its most reprinted story. Use partition for each issue"
	};

	private String[] sqls = {
			"select bg.name from Brand_group bg where bg.pid in (select pid from (select ip.pid, count(ip.iid) as countmax from indicia_publisher ip where ip.cid = (select c.cid from country c where c.name='Belgium') group by ip.pid) where countmax= (select max(bip) from (select ip.pid, count(ip.iid) as bip from indicia_publisher ip where ip.cid = (select c.cid from country c where c.name='Belgium') group by ip.pid)));", 
			"select p.pid, p.name from publisher p where p.pid in (select s.pub_pid from series s where s.country_id= (select c.cid from country c where c.name='Denmark') and s.PUB_TYPE_ID= (select pt.pid from publication_type pt where pt.name='book')) and p.name not like '%nknown%';",
			"select s.name from series s where s.country_id= (select c.cid from country c where c.name='Switzerland') and s.PUB_TYPE_ID= (select pt.pid from publication_type pt where pt.name='magazine');",
			"select year, count(iid) from (select i.iid, to_char(i.publication_date,'YYYY') as year from issue i where i.publication_date >= to_date('1990-01-01','YYYY-MM-DD') and i.publication_date < sysdate) group by year order by year;",
			"select i.pub_id, count(i.sid) as nb from issue i where i.pub_id in (select ip.iid from indicia_publisher ip where ip.name like '%DC Comics%') group by i.pub_id;",
			"select * from story s where s.sid in (select origin from (select count(sr.origin), sr.origin from story_reprint sr group by sr.origin order by count(sr.origin) desc) where rownum<=10);",
			"SELECT A.name FROM Artists A WHERE A.aid in (SELECT W.aid FROM Work_on W WHERE W.script = 1 AND W.pencils = 1 AND W.colors = 1);",
			"SELECT * FROM Story S WHERE S.sid not in (SELECT SR.origin FROM Story_Reprint SR) AND S.sid in (SELECT H.sid FROM Have H WHERE H.feature = 0 AND H.cid in (SELECT C.cid FROM CHARACTERS C WHERE C.name LIKE  'Batman'));",
			"select s.name from series s where s.sid=( select sid from (select count(i.sid) as counted,i.sid as sid from issue i group by i.sid order by counted) where counted=(select max(counts) from (select count(i.sid) as counts from issue i where i.iid not in(select distinct st.iid from (select count(s.sid2) as counted, s.sid2 as tid from story s group by s.sid2), story st where counted=( select max(countType) from (select count(t.sid2) as countType from story t group by t.sid2)) and tid=st.sid2) group by i.sid)));",
			"select p.name from publisher p where p.pid in( select pid from  (select count(distinct st.sid) as nType, p.pid from story_type st, story s, issue i, series se, publisher p where st.sid=s.sid2 and s.iid=i.iid and i.sid=se.sid and se.pub_pid=p.pid group by p.pid) where nType>=(select count(sid) from story_type where name not like '%unknown%'));",
			"select c.name from characters c where c.cid in (select h.cid from have h where h.sid in (select s.sid from story s where s.sid in (select w.sid from work_on w where w.aid in (select a.aid from artists a where a.name like '%Alan Moore%')) and s.sid in (select sr.target from story_reprint sr ))) and c.NAME is not null and rownum<=10;",
			"SELECT * FROM Artists A WHERE A.aid in (SELECT W.aid FROM WORK_ON W WHERE W.script = ‘1’ AND W.pencils = ‘1’ AND W.sid in (SELECT H.sid FROM Has_Genre H WHERE H.gid in (SELECT G.gid FROM Genre G WHERE G.name = ‘nature’)));",
			"with langbypub as (select f2 as lid, f3 as pid from(select f1,f2,f3, row_number() over (partition by f3 order by f1 desc) as rank from (select count(s2.language_id) as f1,s2.language_id as f2,s2.pub_pid as f3 from series s2 where s2.pub_pid in (select pub_pid from( select count(s.pub_pid), s.pub_pid from series s where s.pub_pid !=41 group by s.pub_pid order by count(s.pub_pid ) desc) where rownum<=10) group by s2.language_id,s2.pub_pid)) where rank <=3) select p.name, l.name from langbypub q join language l on l.lid=q.lid join publisher p on p.pid=q.pid order by q.pid;",
			"select serie, lid, count(stories) from (select serie, lid, stories from (select serie, lid, s.sid as stories from (select sname as serie, l.name as lid, i.iid as issid from (select se.name as sname, se.sid as sid1, se.language_id as lang from series se where se.pub_type_id in (select ty.pid from publication_type ty inner join language l on l.lid=se.language_id where ty.name='magazine')) inner join language l on l.lid=lang inner join issue i on i.sid=sid1) inner join story s on s.iid=issid) where stories not in (select r.target from story_reprint r)) group by serie, lid having count(stories)>1000;",
			"SELECT * FROM Story_type WHERE sid in (SELECT S.sid FROM Story S WHERE S.iid in (SELECT I.iid FROM Issue I WHERE I.sid in (SELECT Se.sid FROM Series Se WHERE Se.pub_type_id not in (SELECT T.pid FROM Publication_type T WHERE T.name = ‘magazine') or Se.country_id not in (SELECT C.cid FROM Country C WHERE C.name = 'Italy'))));",
			"select a.name from artists a where a.aid in (select writer from (select writer, count(pid) as pnum from (select writer, issid, i.pub_id as pid from (select writer, story ,s.iid as issid from (select aid as writer, sid as story from work_on where script=1) inner join story s on s.sid = story where s.sid2 in (select st.sid from story_type st where st.name='cartoon')) inner join issue i on issid=i.iid) group by writer order by count(pid) desc) where pnum>1);",
			"select b.name from brand_group b where b.bid in (select bid from (select count(iid),bid from (select bid, bgname, iid from (select bg.pid as bgid, bg.bid, bg.name as bgname from brand_group bg) inner join INDICIA_PUBLISHER p on bgid=p.pid) group by bid order by count(iid) desc) where rownum<=10);",
			"with average as (select pid as publish, round(avg(year_ended-year_began),2) as length from( select pid, s.year_began, s.YEAR_ENDED from (select i.sid as ser, i.pub_id as pid from issue i order by i.pub_id) join series s on s.sid=ser where s.year_ended is not null and s.year_began is not null) group by pid) select p.name, length from average join indicia_publisher p on p.iid=publish order by length desc;",
			"select ip.name from indicia_publisher ip where ip.iid in (select pub_id from (select count(i2.sid), i2.pub_id from issue i2 where i2.sid in (select sid from (select count(i.sid) as counted, i.sid as sid from issue i group by i.sid) where counted=1) group by i2.pub_id order by count(i2.sid) desc) where rownum<=10 and pub_id!=5429);",
			"with counted as (select count(a.aid) as cou,w.sid from artists a join work_on w on a.aid=w.aid where w.script=1 group by w.sid order by count(a.aid) desc) (select i.name from indicia_publisher i where i.iid in (select iid from (select counted.cou,counted.sid,s.iid from counted join story s on s.sid=counted.sid order by counted.cou desc)) and rownum<=10);",
			"with marvel as (select h.cid as heroes from have h where h.sid in (select s.sid from story s where s.iid in (select i.iid from issue i where i.sid in (select s.sid from series s where s.pub_pid in (select p.pid from publisher p where p.name like '%Marvel%' and p.name not like '%/%'))))) select distinct c.name from marvel inner join characters c on c.cid=heroes where heroes in (select h.cid as chara from have h where h.sid in (select s.sid from story s where s.iid in (select i.iid from issue i where i.sid in (select s.sid from series s where s.pub_pid in (select p.pid from publisher p where p.name like '%arvel%DC%' or p.name like '%DC%arvel%')))));",
			"select s.name from series s where s.sid in (select sid from (select count(i.sid) as most, i.sid from issue i group by i.sid order by most desc) where rownum <=5);",
			"with mostreprint as (select f1 as givIssue, f2 as repStory from (select f1, f2, f3, row_number() over (partition by f1 order by f3 desc) as rank from (select iid as f1, r.origin as f2, count(r.target) as f3 from (select s.iid, s.sid from story s) inner join story_reprint r on sid=r.origin group by iid, r.origin)) where rank=1) select givIssue, s.title from mostreprint inner join story s on s.sid=repStory;"
	};

	public Query(int queryNumber){
		if (queryNumber >= 0 && queryNumber < 23){
			name = "Query " + (queryNumber + 1);
			description = descriptions[queryNumber];
			sql = sqls[queryNumber];
		} else {
			name = "Query ?";
			description = "none";
			sql = "";
		}
		x = 105 + (int)(Math.floor(queryNumber / 6.0))*150;
		y = 140 + (queryNumber % 6)*46;
		clicked = false;
		img = new ImageIcon(getClass().getResource("/images/buttons/query.png")).getImage();
		glow = new ImageIcon(getClass().getResource("/images/buttons/queryGlow.png")).getImage();
		click = new ImageIcon(getClass().getResource("/images/buttons/querySelected.png")).getImage();
		button = new Button(img, glow, x, y, name, 5, 15, 14);
	}

	public void draw(Graphics g, Mouse mouse, int mouseX, int mouseY) {
		
		g.setColor(Color.WHITE);
		button.draw(g);
		if (button.isSelected(mouseX, mouseY)) {
			button.glow(g);
			if (mouse.isClickedL()) {
				clicked = true;
			}
		}
		
		if (clicked) {
			g.drawImage(click, x, y, null);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Serif", Font.BOLD, 14));
			g.drawString("Description of the query:", 105, 440);
			g.setFont(new Font("Serif", Font.PLAIN, 14));
			g.drawString("" + description, 105, 460);
			g.setFont(new Font("Serif", Font.BOLD, 14));
		}
	}
	
	public boolean selected(){
		return clicked;
	}
	
	public void done(){
		clicked = false;
	}
	
	public String getSql(){
		return sql;
	}

}
