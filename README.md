#USER SERVICE

##Main
<ul>
<li>Main application is run under profile "local": UserServiceApplication.java.
<li>On application start necessary demo data is created.</li>
<li>Demo data consists of current user and his three email accounts.
One of the accounts has expired quota subscription.</li>
<li>User creation is stubbed. User reference is used to bind email accounts and to set current user into application context.</li>
</ul>

####Demo emails:
<ul>
<li>email1@email.com</li>
<li>email2@email.com</li>
<li>expired_subscription@email.com</li>
</ul>

####API CALLS

<ul>
<li>Get current user summary: curl localhost:8080/api/user</li>
<li>Get current user's account summary: curl -H "Content-Type: application/json" -d '{"email":"email2@email.com"}' http://localhost:8080/api/account</li>
<li>Upgrade quota for current user's account: curl -H "Content-Type: application/json" -d '{"email":"email2@email.com", "quota":"BASIC"}' http://localhost:8080/api/upgrade</li>
</ul>
*In 'email' field any of demo emails can be used.


##Test
Test module contains automated tests for userstories #2 and #6 in user-accounts.feature <br/>

#Architecture

*In Architecture1 emailService is a monolith.<br/> 
Main reasons not to use:<br/>
1. When an infrastructure or configuration issues occur, the whole functionality fails;<br/>
2. Different functionality can not be scaled separately;<br/>
3. Complicated maintenance (deployments, development, feature-testing).<br/><br/>


*In Architecture2 emailService is divided into microservices. But userService and pricingService share the same DB.<br/> 
Main reasons not to use:<br/>
1. Bad practice. Changes made to DB by one microservice can affect the work of another microservice.<br/>
For example, userService drops a column in userStore which is used by pricingService.<br/>
This will cause an unexpected failure of pricing service.
2. Taking in consideration #1 reason, userService and pricingService are not able to use some benefits of microservice architecture (independent deployments and development)


#Summary
<ul>
<li>Time consumption: ~10 hours</li>
<li>Main challenge: Abstraction from other features. It took some time to focus only on userstory #2 and #6 and not to implement user registration, validations and so on. </li>
</ul>
