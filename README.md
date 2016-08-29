## Generic DAO JPA Project ##

This project created a library for Generic Data Access Object (DAO) which follows JPA specification to perform CRUD and avoid those redundant codes.

The DAO supports the variety of different querying and "order by" methods and also the paging mechanism.

In addition, this project also uses Aspect-Oriented Programming (AOP) skill to log each request and response contents without interrupt the business logic.

### Spring RESTful example ###

#### Create a company  ####

http://localhost:8090/generic_dao_jpa/company

Http POST request

> {
> "companyName" : "ExampleCompany"
> }

Http response

> {
>     "companyId": 1,
>     "companyName": "ExampleCompany"
> }

Java Code
> 	@RequestMapping(method=RequestMethod.POST, consumes="application/json")

> 	public @ResponseBody Company createCompany(@RequestBody Company company)	{

> 		company = companyDao.save(company);

> 		return company;

> 	}

#### Select for company ####

***Select by Company Id***

http://localhost:8090/generic_dao_jpa/company/1

Http GET request

**None**

Http response

> {
>     "companyId": 1,
>     "companyName": "ExampleCompany"
> }

Java Code
> 	@RequestMapping(path="/{companyId}", method=RequestMethod.GET)

> 	public @ResponseBody Company getCompany(@PathVariable("companyId") Long companyId)	{

> 		Company result = companyDao.setClazz(Company.class).findOne(companyId);

> 		return result;

> 	}



***Select by Company Name***

http://localhost:8090/generic_dao_jpa/company/name/ExampleCompany

Http GET request

**None**

Http response

> {
>     "companyId": 1,
>     "companyName": "ExampleCompany"
> }

Java Code
> 	@RequestMapping(path="/name/{companyName}", method=RequestMethod.GET)

> 	public @ResponseBody List<CompanygetCompany(@PathVariable("companyName") String companyName)	{

> 		List<Companyresult = companyDao.setClazz(Company.class).findBy(new String[]{"companyName"}, new String[]{companyName});

> 		return result;

> 	}


#### Update a company ####

http://localhost:8090/generic_dao_jpa/company/1

Http PUT request

> {
> "companyName" : "UpdatedCompany"
> }

Http response

> {
>     "companyId": 1,
>     "companyName": "UpdatedCompany"
> }

Java Code
> 	@RequestMapping(path="/{companyId}",method=RequestMethod.PUT, consumes="application/json")

> 	public @ResponseBody Company updateCompany(@PathVariable Long companyId, @RequestBody Company company)	{

> 		company.setCompanyId(companyId);

> 		company = companyDao.save(company);

> 		return company;

> 	}

#### Delete a company ####

http://localhost:8090/generic_dao_jpa/company/1

Http DELETE request

**None**

Http response

**None**

Java Code
> 	@RequestMapping(path="/{companyId}", method=RequestMethod.DELETE)

> 	public void deleteCompany(@PathVariable Long companyId)	{

> 		companyDao.setClazz(Company.class).deleteById(companyId);

> 	}


### Message Log ###
Java Code
> 	@Around("execution(* com.rontseng.example.controller..*(..))")

> 	public Object logAroundMessage(ProceedingJoinPoint joinPoint) throws Throwable {

> 		ObjectMapper mapper = new ObjectMapper();

> 		MessageLog log = new MessageLog();

> 		log.setUrl(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()).toString());

> 		log.setHttpMethod(httpRequest.getMethod());

> 		Object request = joinPoint.getArgs()[0];

> 		log.setRequestContent(mapper.writeValueAsString(request));

> 		log.setRequestTime(DateUtil.getCurrentDate());

> 		Object result = joinPoint.proceed();

> 		log.setResponseContent(mapper.writeValueAsString(result));

> 		log.setResponseTime(DateUtil.getCurrentDate());
 
> 		messageLogDao.save(log);

> 		return result;

> 	}
