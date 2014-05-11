The following table describes all technical details of Use Case `UC7`:

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
            <td>UC7</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>_self</code> terminates</td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>User</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td/>
         </tr>
         <tr>
            <td>Brief</td>
            <td>"The user just removes the repository and we immediately terminate the Docker container, close all ports, and stops routing domain names. This operation is irrevertable"</td>
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
      </tbody>
   </table>
