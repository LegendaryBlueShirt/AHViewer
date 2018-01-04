package application;

import java.util.Arrays;
import java.util.List;

import view.model.CharacterDef;

public class AHCharacters {
	public enum AHCharacter implements CharacterDef {
		HEART	("Heart Aino","chara",0,8,5),
		SAKI	("Saki Tsuzura","chara",1,6,8),
		KAMUI	("Kamui Tokinomiya","chara",2,9,10),
		KONOHA	("Konoha","chara",3,5,7),
		MAORI	("Maori Kasuga","chara",4,6,5),
		MEIFANG	("Mei-Fang","chara",5,10,4),
		LILICA	("Lilica Felchenerow","chara",6,5,8),
		LIESELOTTE	("Lieselotte Achenbach","chara",7,4,3),
		YORIKO	("Yoriko Yasuzumi","chara",8,11,8),
		KIRA	("Kira Daidohji","chara",9,12,0),
		FIONA	("Fiona Mayfield","chara",10,8,12),
		MILDRED	("Mildred Avallone","chara",11,1),
		PETRA	("Petra Johanna Lagerkvist","chara",12,8,10),
		ZENIA	("Zenia Valov","chara",13,10,5),
		ANGELIA	("Angelia Avallone","chara",14,6,7),
		ELSA	("Elsa La Conti","chara",15,8,9),
		CLARICE	("Clarice Di Lanza","chara",16,10,7),
		CATHERINE	("Catherine Kyobashi","chara",17,15,3),
		DOROTHY	("Dorothy Albright","chara",18,5,4),
		AKANE	("Akane Inukawa","chara",20,8,9),
		NAZUNA	("Nazuna Inukawa","chara",21,15,5),
		PARACE	("Parace L'sia","chara",22,6,7),
		SCHARLACHROT	("Scharlachrot","chara",23,9,16),
		EKO	("Eko","chara",24,12,4),
		WEISS	("Weiss","chara",25,8,10),
		RAGNAROK	("Ragnarok","chara",27,1,2),
		MINORI("Minori Amanohara","chara", 28, 1, 1),
		//EFFECT	("Effects","effect",0,11,1,10), Will Fix Some Other Time
		LOVE	("Love - Partinias","tenshi",0,3,2),
		LIGHTNING	("Lightning - Bhanri","tenshi",1,2,2),
		TIME	("Time - Anutpada","tenshi",2,1,1),
		PLANT	("Plant - Moriomoto","tenshi",3,2,1),
		EARTH	("Earth - Ohtsuchi","tenshi",4,2,1),
		FIRE	("Fire - Lang-Gong","tenshi",5,3,2),
		WIND	("Wind - Tempestas","tenshi",6,2,2),
		DARK	("Dark - Gier","tenshi",7,3,1),
		EVIL	("Evil - Dieu Mort","tenshi",8,1,6),
		WATER	("Water - Niptra","tenshi",9,3,2),
		METAL	("Metal - Oreichalkos","tenshi",10,1,5),
		HOLY	("Holy - Zillael","tenshi",12,3,1),
		ICE	("Ice - Almacia","tenshi",13,5,1),
		HALO	("Halo - Mildred","tenshi",14,3,4),
		PUNISHMENT	("Punishment - Koshmar","tenshi",15,2,3),
		SIN	("Sin - Sorwat'","tenshi",16,2,1),
		MAGNET	("Magnetism - Medein","tenshi",17,2,1),
		MIRROR	("Mirror - Heliogabalus","tenshi",18,1,3),
		HALOB	("Halo - Mildred (Boss)","tenshi",19,3,4),
		TONE	("Tone - Phenex","tenshi",20,1,6),
		FLOWER	("Flower - Kayatushime","tenshi",21,3,3),
		FENRIR	("Fenrir - Baldur","tenshi",23,4,6),
		LUCK	("Luck - Saligrama","tenshi",24,1,14),
		TYR	("Tyr - Gottfried","tenshi",25,5,9),
		FENRIRB	("Fenrir - Baldur (Boss)","tenshi",26,4,6),
		//ARAGNAROK	("Ragnarok - Ragnarok","tenshi",27,0,6);
		BLOOD("Blood - Ichor", "tenshi", 28, 1, 1);
		
		private int num,sprites=-1,sprites2=-1;
	    private String name;
	    private String subfolder;
	    boolean hasExtra = true;
	    AHCharacter(String name,String subfolder,int num,int sprites) {
	        this.num = num;
	        this.sprites = sprites;
	        this.subfolder=subfolder;
	        this.name = name;
	        hasExtra = false;
	    }
	    AHCharacter(String name,String subfolder,int num,int sprites,int sprites2) {
	        this.num = num;
	        this.sprites = sprites;
	        this.sprites2 = sprites2;
	        this.subfolder=subfolder;
	        this.name = name;
	    }
	    
	    @Override
	    public String toString() {
	    		return name;
	    }
	    
	    public boolean hasExtra()
	    {
	    		return hasExtra;
	    }
	    
	    public String getDataFile() {
	    		return String.format("chara/act_%02d.pk3", num);
	    }
	    
	    public String getPacFile() {
	    		return String.format("chara/chara_split_%02d.pac", num);
	    }
	}
	
	public static List<AHCharacter> getCharacters() {
		return Arrays.asList(AHCharacter.values());
	}
}
