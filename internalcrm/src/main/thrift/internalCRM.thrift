namespace java org.example.internalcrm.thrift

typedef i32 int
typedef i64 long

struct InternalLeadDto {
    1: int ID
    2: string fullName
    3: double annualRevenue
    4: string phone
    5: string street
    6: string postalCode
    7: string city
    8: string country
    9: long creationDate
    10: string company
    11: string state
}

exception InvalidRevenueRangeException {
    1: string message
    2: double lowAnnualRevenue
    3: double highAnnualRevenue
}

exception InvalidDateException {
    1: string message
    2: long startDate
    3: long endDate
}

exception LeadNotFoundException {
    1: string message
    2: int ID
}

exception LeadAlreadyExistsException {
    1: string message
    2: int ID
}

exception InvalidLeadParameterException {
    1: string message
}

//rajout => capture erreur inattendue côté serveur
exception InternalErrorException {
    1: string message
}


service InternalCRMService {
    list<InternalLeadDto> findLeads (
            1: double lowAnnualRevenue,
            2: double highAnnualRevenue,
            3: string state)
        throws (
            1: InvalidRevenueRangeException e)

    list<InternalLeadDto> findLeadsByDate (
            1: long startDate,
            2: long endDate)
        throws (
            1: InvalidDateException e)

     //accès à un lead par id (pas besoin de parcourir liste)
    // plus facile pour faire "GET /leads/{id}" (que fera le service REST)
    InternalLeadDto getLeadById(1: int ID)
        throws (1: LeadNotFoundException e)



    void deleteLead (
            1: int ID)
        throws (
            1: LeadNotFoundException e)

    int addLead (
            1: string fullName,
            2: double annualRevenue,
            3: string phone,
            4: string street,
            5: string postalCode,
            6: string city,
            7: string country,
            8: string company,
            9: string state
            )
        throws (
            1: LeadAlreadyExistsException e,
            2: InvalidLeadParameterException ee)

    list<InternalLeadDto> getLeads ()


    //méthode pour nb total lead
    int countLeads()


}