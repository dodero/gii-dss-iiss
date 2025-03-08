public class CreditCard {

    private String number;
    private String expirationDate;
    private String name;
    private int cvv;

    public CreditCard(String number, String expirationDate, String name, int cvv) {
        if(!expirationDate.matches("\\d{2}/\\d{2}"))
            throw new IllegalArgumentException("Expiration Date must be in format MM/YY");
        
        int month = Integer.parseInt(expirationDate.substring(0,2));

        if(month > 12 || month < 1)
            throw new IllegalArgumentException("The expiration month must be between 1 and 12");
        
        
        this.number = number;
        this.expirationDate = expirationDate;
        this.name = name;
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getName() {
        return name;
    }

    public int getCvv() {
        return cvv;
    }

}
