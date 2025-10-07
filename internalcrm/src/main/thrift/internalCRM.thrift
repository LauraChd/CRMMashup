namespace java org.example.internalcrm.thrift

typedef i32 int
typedef i64 long

struct InternalLeadDto {
    1: string fullName
    2: double annualRevenue
    3: string phone
    4: string street
    5: string postalCode
    6: string city
    7: string country
    8: long creationDate
    9: string company
    10: string state
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
    2: InternalLeadDto lead
}

exception LeadDoesNotExistException {
    1: string message
    2: InternalLeadDto lead
}

exception LeadAlreadyExistsException {
    1: string message
    2: InternalLeadDto lead
}

exception InvalidLeadParameterException {
    1: string message
    2: InternalLeadDto lead
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

    void deleteLead (
            1: int ID)
        throws (
            1: LeadNotFoundException e)

    void addLead (
            1: string fullName
            2: double annualRevenue
            3: string phone
            4: string street
            5: string postalCode
            6: string city
            7: string country
            8: string company
            9: string state)
        throws (
            1: LeadDoesNotExistException e,
            2: LeadAlreadyExistsException ee,
            3: InvalidLeadParameterException eee)
}