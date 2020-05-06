package Robot;

public class Measure {
	public Point location = null;
    public double pM25 = 0.0;
    public int minute = 0;
    
    public Measure(Point locationA, double pM25A, int minuteA)    {
        this.location = locationA;
        this.pM25 = pM25A;
        this.minute = minuteA;
    }
    
    public String getQualityRange()   {
        if (Math.round(pM25) < 50)  {
            return "Good";
        } else if (Math.round(pM25) < 100)  {
            return "Moderate";
        } else if (Math.round(pM25) < 150)  {
            return "USG (Unhealthy to Sensitive Groups)";
        } else  {
            return "Unhealthy";
        }
    }
}
