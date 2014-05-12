The following table describes all technical details of Use Case `UC9.2`:

<table>
      <thead>
         <tr>
            <th>Property</th>
            <th>Details</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>ID</td>
            <td>UC9.2</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>anonymous</code> signs up</td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>Anonymous</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td>
               <code>anonymous</code> as Anonymous; <code>user</code> as User</td>
         </tr>
         <tr>
            <td>Success Flow</td>
            <td>1. <code>anonymous</code> "initiates sign in via one of auth providers, e.g., Facebook, Google+, etc."<br/>2. <code>system</code> "requests permission from the OAuth provider chosen by the anonymous"<br/>3. <code>system</code> creates <code>user</code>
               <br/>4. <code>system</code> "adds $5 to the user.balance"<br/>5. <code>anonymous</code> logs in</td>
         </tr>
      </tbody>
   </table>

Actors taking participation in the Use Case have the following properties:

<table>
      <thead>
         <tr>
            <th>Actor</th>
            <th>Properties</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>Anonymous</td>
            <td>"a person who is not identified in the system (not logged in). He can actually have a User account in our System, but we can't tell it until he logs in"</td>
         </tr>
         <tr>
            <td>Anonymous</td>
            <td>"a person who is not identified in the system (not logged in). He can actually have a User account in our System, but we can't tell it until he logs in"</td>
         </tr>
         <tr>
            <td>User</td>
            <td>"a person identified in the system (logged in)"<ul>
                  <li>
                     <code>repo</code>: Repository</li>
                  <li>
                     <code>URN</code>:  "a unique identifier of itself, for example urn:github:526301"</li>
                  <li>
                     <code>authTokens</code>:  "a list of auth tokens to each auth provider the user connected to, e.g., Goole, Facebook, etc."</li>
                  <li>
                     <code>balance</code>:  "amount of money available, can be stored as integer number of cents"</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
