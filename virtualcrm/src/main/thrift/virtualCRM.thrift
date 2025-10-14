namespace java org.example.internalcrm.thrift

typedef i32 int
typedef i64 long

struct VirtualLeadDto {
    1: int ID
    2: string firstName
    3: string lastName
    4: double annualRevenue
    5: string phone
    6: string street
    7: string postalCode
    8: string city
    9: string country
    10: long creationDate
    11: string company
    12: string state
    13: GeographicPointDto geographicPosition
}

struct GeographicPointDto {
    1: double latitude
    2: double longitude
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
    2: string fullName
    3: int ID
}

exception InvalidLeadParameterException {
    1: string message
}

service VirtualCRMService {
    list<VirtualLeadDto> findLeads (
            1: double lowAnnualRevenue,
            2: double highAnnualRevenue,
            3: string state)
        throws (
            1: InvalidRevenueRangeException e)

    list<VirtualLeadDto> findLeadsByDate (
            1: long startDate,
            2: long endDate)
        throws (
            1: InvalidDateException e)

    void deleteLead (
            1: int ID)
        throws (
            1: LeadNotFoundException e)

    int addLead (
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
            1: LeadAlreadyExistsException e,
            2: InvalidLeadParameterException ee)

    list<VirtualLeadDto> getLeads ()
}