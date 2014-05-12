The following table describes all technical details of Use Case `UC1`:

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
            <td>UC1</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>user</code> hosts <code>repo</code>
            </td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>User</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td>
               <code>user</code> as User; <code>repo</code> as Repository</td>
         </tr>
         <tr>
            <td>Success Flow</td>
            <td>1. <code>user</code> registers <code>repo</code>
               <br/>2. <code>repo</code> deploys itself<br/>3. <code>user</code> reads deployment logs using <code>repo</code>
               <br/>4. <code>user</code> reads usage stats using <code>repo</code>
               <br/>5. <code>user</code> compensates usage using <code>repo</code>
               <br/>6. <code>user</code> terminates <code>repo</code>
            </td>
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
         <tr>
            <td>Repository</td>
            <td>"a local copy of files and folders being pulled every 5 minutes from some remote source"<ul>
                  <li>
                     <code>name</code>:  "a unique name of the repo in user's account"</li>
                  <li>
                     <code>deployment</code>: Deployment</li>
                  <li>
                     <code>dockerFile</code>:  "Docker file"</li>
                  <li>
                     <code>manifest</code>: Manifest</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
