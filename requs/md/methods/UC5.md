The following table describes all technical details of Use Case `UC5`:

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
            <td>UC5</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>user</code> reads usage stats using <code>repo</code>
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
            <td>1. <code>user</code> "selects a time interval, like: last 7 days, last month, custom (from date, to date)"<br/>2. <code>system</code> "reads HourlyUsage records corresounding to the repo within specified time interval"<br/>3. <code>system</code> "shows the user a table with these columns: Container (id), Inbound traffic (Gb),
    Outbound traffic (Gb), CPU time (min), Cost ($). Each table row corresponds to a container.
    Last row contains totals by all containers. The values in the table are usage stats by container
    within specified time interval"</td>
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
            <td>"a storage of source code together with Dockerfile"<ul>
                  <li>
                     <code>name</code>:  "a unique name of the repo in user's account"</li>
                  <li>
                     <code>URI</code>:  "a non-ambiguous descriptor of a repo, for example ssh://git@github:yegor256/thindeck.git"</li>
                  <li>
                     <code>key</code>:  "a private SSH key, see http://en.wikipedia.org/wiki/Public-key_cryptography"</li>
                  <li>
                     <code>deployment</code>: Deployment</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
